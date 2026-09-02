# DFS — App APIs: Enhancements

**Module:** App APIs (`com.dfs:app`) · Spring Boot 2.7.5 (WAR on Tomcat)
**Date:** 2026-06-20

## Summary
This release adds **interactive API documentation**, **end-to-end request tracing**, **PII-safe logging**, and several **performance, scalability & reliability** improvements — delivered **without any new external infrastructure** and **without changing existing business logic or endpoints**. The work is especially aligned with a high-scale, financial-grade workload (data integrity, no audit loss, no sensitive data in logs).

## At a glance

| # | Enhancement | Category | Key benefit |
|---|-------------|----------|-------------|
| 1 | Interactive API docs (Swagger / OpenAPI 3) | API / Integration | Faster partner & QA onboarding; live "try-it-out" with JWT |
| 2 | Distributed request tracing (traceId in every log) | Observability | Follow one request end-to-end across services; faster production support |
| 3 | PII-safe, trimmed logging | Security / Compliance | Sensitive data masked in logs; lower log volume/cost at scale |
| 4 | Asynchronous audit logging | Performance / Compliance | Lower API latency under load; **no audit record lost** |
| 5 | Reference-data caching (in-memory) | Performance | Lookup endpoints served from memory; large drop in DB load |
| 6 | Outgoing-call timeouts | Reliability | A slow/down dependency can't freeze the whole service |
| 7 | JSON processor reuse | Performance | Less CPU/GC per request → higher throughput |
| 8 | Startup cleanup | Code quality | Removed an invalid-injection startup warning |

---

## Details

### 1. Interactive API Documentation (Swagger / OpenAPI 3)
- Auto-generated, always-up-to-date API documentation with a built-in **"Authorize" (JWT)** button for live testing.
- **Access:** `…/app/swagger-ui.html` (spec: `…/app/v3/api-docs`).
- **Benefit:** partners, QA and new developers can explore and test every endpoint without external tools — faster integration.
- **Tech:** `springdoc-openapi-ui`, `OpenApiConfig`. Can be disabled per-environment (recommended off in production).

### 2. Distributed Request Tracing
- Every log line now carries a **service name + traceId + spanId**, e.g. `[app] [a1b2c3…,a1b2c3…]`.
- The traceId **propagates over outgoing REST calls**, so a single request can be followed **end-to-end across multiple services** sharing one log.
- **Benefit:** dramatically faster debugging, incident analysis and audit/forensics in production.
- **Tech:** Spring Cloud Sleuth + `logback-spring.xml`; instrumented HTTP clients. No external server required.

### 3. PII-Safe, Trimmed Logging
- Logs are concise by default (no payloads at INFO), keeping log volume and cost low at scale.
- **POST request & response bodies** are captured for support — including **error/exception responses** — but with **sensitive fields masked** (password, PIN/MPIN, OTP, CVV, CNIC, Tazkira, mobile, card number, account number, IBAN, balance, etc.) and truncated.
- **GET responses are not logged** (reference/list data — noise + privacy).
- **Benefit:** compliance & privacy (no sensitive data leaks into logs) without losing supportability.
- **Tech:** `LoggingAspect` + shared `LogSanitizer`.

### 4. Asynchronous Audit Logging
- The per-request audit (request/response persisted to the database) now runs **off the request thread** — the response is returned to the user **first**, then the audit is written in the background.
- Uses a **no-loss** policy: under heavy load the audit still completes (never silently dropped), and in-flight audits are flushed on shutdown — **compliance-grade** for a financial system.
- **Benefit:** noticeably lower API latency under load, while keeping a complete audit trail.
- **Tech:** `AsyncConfig` (dedicated bounded executor, CallerRunsPolicy) + refactored `RequestResponseLoggingFilter`.

### 5. Reference-Data Caching (In-Memory)
- Lookup / static endpoints (provinces, districts, LOVs, FAQs, tutorials, app-screen content, etc.) are now served from an **in-memory cache** instead of hitting the database on every request.
- **Auto-refresh:** a lightweight background check detects when reference data changes and refreshes only the affected cache; a time-based expiry is the safety net.
- **Safe by design:** only static reference data is cached — **never money, account, balance, OTP or auth data**.
- **Benefit:** these endpoints respond in microseconds and **database load drops sharply** — and the cache is **bounded & load-independent** (it does not grow with user traffic).
- **Tech:** Spring Cache + Caffeine (`CacheConfig`, `CacheRefreshScheduler`).

### 6. Outgoing-Call Timeouts
- All outbound calls (OTP, notifications, card services, geocoding, Google-auth) now have **connect & read timeouts**.
- **Benefit:** a slow or unreachable dependency can no longer hold server threads indefinitely (previously a single dependency could stall requests for ~21s) — the service stays responsive during partial outages.
- **Tech:** `HttpClientConfig` (RestTemplate timeouts + WebClient connector). Values are configurable (default connect 3s, read 30s).

### 7. JSON Processor Reuse
- Eliminated repeated creation of the JSON processor (`ObjectMapper`) — previously created multiple times per request — in favour of a single shared, thread-safe instance.
- **Benefit:** less CPU and garbage-collection overhead per request → better throughput at scale.

### 8. Startup Cleanup
- Removed a dead field that produced an "Autowired annotation is not supported on static fields" warning at startup.
- **Benefit:** cleaner, warning-free startup and logs.

---

## Notes
- **No new infrastructure** was introduced (no message broker, no external cache/DB) — all enhancements run inside the existing Spring Boot + Oracle stack.
- **No business logic or API contracts changed** — endpoints behave exactly as before; these are quality, performance, security and observability improvements.
- Key behaviours are **configurable** (timeout values, cache TTLs, audit-refresh interval, Swagger on/off, log levels).

## Recommended Next Steps (roadmap — not in this release)
Discussed and ready to schedule when needed, prioritised for a financial app at scale:
- **Idempotency keys** for financial write operations (prevent double-processing on client retries).
- **Externalised secrets** (move DB/mail credentials to environment/secret store + rotate).
- **HikariCP tuning + `open-in-view=false`** for database connection efficiency.
- **Distributed cache / rate-limiting (Redis)** if/when multi-instance scale requires it.
