package com.dfs.agentapp.config;

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
                        .title("Agent App APIs")
                        .description("DFS Agent App backend services")
                        .version("v1")
                        .contact(new Contact().name("DFS")))
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
