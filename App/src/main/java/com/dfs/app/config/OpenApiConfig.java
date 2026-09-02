package com.dfs.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3 / Swagger UI configuration (springdoc-openapi).
 *
 * Swagger UI:  {context-path}/swagger-ui.html   e.g. http://localhost:1584/app/swagger-ui.html
 * OpenAPI JSON:{context-path}/v3/api-docs        e.g. http://localhost:1584/app/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI dfsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DFS App APIs")
                        .description("Mobile-facing REST APIs for the DFS branchless banking app "
                                + "(onboarding, KYC/identity, accounts, debit cards, QR & FavPay payments, lookups). "
                                + "Most write operations accept the standard request envelope: { \"payload\": { ... } }.")
                        .version("v1")
                        .contact(new Contact().name("DFS")))
                // Relative server URL: Swagger UI's "Try it out" calls go to the SAME origin that served
                // the page (e.g. https://your-host/app), not the internal host the app sees
                // behind a reverse proxy (127.0.0.1). Fixes the wrong-host / CORS problem.
                .addServersItem(new Server().url("/app"))
                // Declares the JWT bearer scheme so the "Authorize" button is available in Swagger UI.
                // Pass the token returned by /v1/login. Note: open endpoints (login, registration,
                // lookups, public content) ignore the header.
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
