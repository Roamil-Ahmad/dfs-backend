# Adding OpenAPI (Swagger) + Distributed Tracing (Sleuth) to a Service

A copy-paste guide to add **Swagger/OpenAPI docs** and **distributed tracing + correlated logging** to any of our Spring Boot services.

**Applies to:** Spring Boot **2.7.x**, Maven, WAR deployed to a shared external **Tomcat 9** (`javax.*` namespace), JDK ≥ 13 (17 recommended).

> Two parts, independent — you can do either alone:
> - **Part A — OpenAPI / Swagger** (`springdoc`)
> - **Part B — Distributed Tracing + Logging** (`spring-cloud-sleuth` + `logback-spring.xml`)
>
> ⚠️ **The single most important per-service step:** give each service its **own unique** `spring.application.name` (Part B, step 2). That name is what tells services apart in the shared `catalina.out`.

---

## Part A — OpenAPI / Swagger

### A1. Add the dependency (`pom.xml`)
```xml
<!-- OpenAPI 3 spec + Swagger UI. 1.x line is for Spring Boot 2.7 (do NOT use 2.x — that's Boot 3). -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.8.0</version>
</dependency>
```

### A2. Add config class `config/OpenApiConfig.java`
Sets the API title/version and a JWT **Authorize** button. Change the package, title and description per service.
```java
package com.barkatpay.<service>.config;   // <-- adjust package

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("<Service Name> APIs")              // <-- per service
                        .description("<one-line description>")     // <-- per service
                        .version("v1")
                        .contact(new Contact().name("BarakatPay")))
                // JWT bearer scheme -> enables the "Authorize" button in Swagger UI.
                // Remove the addSecurityItem line if a service has only public endpoints.
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT issued by /v1/login, sent in the Authorization header.")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME));
    }
}
```

### A3. Add properties (`application.properties`)
```properties
# ---- springdoc / Swagger UI ----
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.disable-swagger-default-url=true
springdoc.swagger-ui.enabled=true
springdoc.api-docs.enabled=true
```
> 🔒 **Production:** set `springdoc.swagger-ui.enabled=false` (we have no Spring Security, so the UI is otherwise open).

### A4. (Only if the service has a global request-logging/audit filter)
If a service persists/logs a row **per request** (e.g. a `RequestResponseLoggingFilter`), exclude Swagger assets so a single page load doesn't create dozens of rows. Add this override to that filter:
```java
@Override
protected boolean shouldNotFilter(javax.servlet.http.HttpServletRequest request) {
    String uri = request.getRequestURI();
    return uri.contains("swagger") || uri.contains("api-docs") || uri.contains("webjars");
}
```

### A5. Access (mind the context path)
The WAR is served under its context path, so under context `/app`:
- Swagger UI → `http://<host>:<tomcat-port>/app/swagger-ui.html`
- OpenAPI JSON → `http://<host>:<tomcat-port>/app/v3/api-docs`

---

## Part B — Distributed Tracing + Logging

**Goal:** every log line carries `[serviceName] [traceId,spanId]`, and the `traceId` propagates over REST calls between services — so one request can be followed end-to-end across the shared `catalina.out`.

### B1. Add the Spring Cloud BOM + Sleuth starter (`pom.xml`)
Add the BOM (manages the Sleuth version — **don't pin Sleuth itself**). If a `<dependencyManagement>` block already exists, **merge** this `<dependency>` into it instead of adding a second block.
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2021.0.8</version>   <!-- compatible with Spring Boot 2.7.x -->
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```
Then in `<dependencies>` (no version — the BOM resolves it, ~3.1.x):
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```

### B2. Set a UNIQUE service name (`application.properties`) ⚠️
```properties
spring.application.name=<your-service-name>
```
Use a **distinct, meaningful** name per service (e.g. `app`, `thirdparties`, `cardwrapper`, `googleauth`). This is what appears as `[name]` in every log line and is the only way to tell services apart in the shared `catalina.out`.

### B3. Add `src/main/resources/logback-spring.xml`
If the file already exists, only add the `<springProperty>` line and the `[${appName}] [%X{traceId:-},%X{spanId:-}]` part to the existing console `<pattern>` — don't remove other appenders.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>
    <!-- Binds the Spring property so the name actually prints (Sleuth puts only traceId/spanId in MDC). -->
    <springProperty scope="context" name="appName" source="spring.application.name" defaultValue="-"/>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [${appName}] [%X{traceId:-},%X{spanId:-}] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```
> ⚠️ Must be named `logback-spring.xml` (NOT `logback.xml`) — `<springProperty>` only works in the `-spring` variant.
> ⚠️ Use `[${appName}]`, **not** `[%X{spring.application.name:-}]` — Sleuth does not put the app name in MDC, so the `%X` form prints blank.

### B4. Instrument outgoing REST clients — **CONDITIONAL** (read carefully)
Sleuth only propagates the `traceId` over calls made with **Spring-managed, instrumented** clients. First decide what the service is:

| The service… | Do this |
|---|---|
| makes **no** outgoing calls (leaf) | **Nothing** — skip B4 entirely |
| uses **Feign** | **Nothing** — Feign is auto-instrumented |
| uses **`new RestTemplate()`** | Replace with an injected bean (below) |
| uses **`WebClient.create()` / static `WebClient.builder()`** | Use an **injected** `WebClient.Builder` (below) |

**🚨 Do NOT create a duplicate `RestTemplate` bean.** If a `@Bean RestTemplate` already exists anywhere, leave it (Sleuth instruments it automatically). Adding a second one → `BeanDefinitionOverrideException`.

**RestTemplate — add the bean only if none exists** (`config/HttpClientConfig.java`):
```java
package com.barkatpay.<service>.config;   // <-- adjust package

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
```
Then inject and use it instead of `new RestTemplate()`:
```java
@Autowired
private RestTemplate restTemplate;
// ... use restTemplate.exchange(...) — was: RestTemplate restTemplate = new RestTemplate();
```

**WebClient — inject the auto-configured builder** (no bean needed; Sleuth instruments `WebClient.Builder`):
```java
@Autowired
private WebClient.Builder webClientBuilder;

// before:  WebClient wc = WebClient.builder().baseUrl(url).build();
// after:   WebClient wc = webClientBuilder.clone().baseUrl(url).build();
```
> `.clone()` because the injected builder is reused per bean — cloning avoids one request's `baseUrl` leaking into another.

> 📌 **Worked example in this repo:** all outgoing calls live in `HelperClass` — `new RestTemplate()` and static `WebClient.builder()` were swapped for the injected beans above, and `HttpClientConfig` was added (no prior RestTemplate bean existed).

### B5. Async propagation — **CONDITIONAL**
- Default `@Async` (no custom executor), `@Scheduled` → **nothing to do**; Sleuth auto-instruments them.
- Only if the service defines its **own** `Executor` / `ExecutorService` / `ThreadPoolTaskExecutor` bean (or `CompletableFuture.supplyAsync(..., customExecutor)`), wrap it so the trace crosses the thread boundary:
```java
return new org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor(beanFactory, delegateExecutor);
```
> This repo uses one `@Async` method on the default executor → no wrapping needed.

---

## Verify (per service)
```bash
# 1. Builds & packages
./mvnw clean package -DskipTests          # -> target/<finalName>.war ; BUILD SUCCESS
./mvnw dependency:tree | grep -iE "sleuth|springdoc"   # confirm both resolve

# 2. Run, then check the log shows the new format with a populated traceId on a request:
#    2026-06-14 20:31:17 [your-service] [78385fce...,78385fce...] INFO ... LoggingAspect - Entering method: ...
#    (startup lines show [name] [,] — empty trace is expected before any request)

# 3. Swagger UI loads at  http://<host>:<port>/<context>/swagger-ui.html
```
**Cross-service check:** trigger a flow where service A calls service B (both with Sleuth). The **same `traceId`** should appear in both services' lines in `catalina.out`.

---

## Per-service checklist
- [ ] `springdoc-openapi-ui:1.8.0` added
- [ ] `OpenApiConfig.java` added (title/desc/package set per service)
- [ ] springdoc properties added (`enabled=false` for prod)
- [ ] Swagger paths excluded from any per-request audit filter
- [ ] Spring Cloud BOM `2021.0.8` added/merged + `spring-cloud-starter-sleuth`
- [ ] **`spring.application.name` set to this service's UNIQUE name**
- [ ] `logback-spring.xml` added (`[${appName}] [traceId,spanId]`)
- [ ] Outgoing clients instrumented **only if a caller** (no duplicate `RestTemplate` bean!)
- [ ] Custom executors wrapped with `LazyTraceExecutor` (only if any exist)
- [ ] Verified: build OK, `traceId` in request logs, Swagger UI loads

---

## Notes / gotchas
- **Every service needs Sleuth** for end-to-end tracing — a non-Sleuth service breaks the chain (its lines have no `traceId`).
- **`server.port` / `server.servlet.context-path` are ignored on external Tomcat** — the port is Tomcat's connector and the context path comes from the WAR filename.
- **Sampling doesn't affect logs** — `traceId`/`spanId` appear in 100% of log lines regardless. `spring.sleuth.sampler.probability` only matters if you export spans to a backend.
- **Compatibility:** keep to the **1.x** springdoc line and Spring Cloud **2021.0.x** while on Spring Boot 2.7. Upgrading to Boot 3 later requires springdoc 2.x, Spring Cloud 2022.x, and Micrometer Tracing (Sleuth is replaced).
