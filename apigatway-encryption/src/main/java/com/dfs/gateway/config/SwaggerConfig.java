package com.dfs.gateway.config;

import com.dfs.gateway.dto.EncryptedRequest;
import com.dfs.gateway.dto.EncryptedResponse;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger configuration including inventory-driven endpoint documentation.
 */
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private static final String ENCRYPTION_NOTE =
            "Request body must be AES-256-GCM encrypted. See EncryptedRequest schema.";
    private static final String BEARER_SCHEME = "bearerAuth";

    private final ApiInventoryLoader apiInventoryLoader;

    /**
     * Base OpenAPI metadata for the crypto gateway.
     *
     * @return OpenAPI definition
     */
    @Bean
    public OpenAPI dfsOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Barkat Pay Crypto Gateway")
                        .version("1.0.0")
                        .description("Encrypted API Gateway — all requests and responses are AES-256-GCM encrypted")
                        .contact(new Contact().name("DFS")))
                // JWT bearer scheme -> enables the "Authorize" button in Swagger UI.
                // The gateway forwards the Authorization header to downstream services.
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT issued by the login flow, sent in the Authorization header.")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME));
    }

    /**
     * Registers one documented operation per unique route from {@code api-inventory.json}.
     *
     * @return OpenAPI customizer bean
     */
    @Bean
    public OpenApiCustomiser inventoryOpenApiCustomizer() {
        return openApi -> {
            registerEnvelopeSchemas(openApi);

            Paths paths = openApi.getPaths();
            if (paths == null) {
                paths = new Paths();
                openApi.setPaths(paths);
            }

            for (ApiInventoryLoader.InventoryEndpoint endpoint : apiInventoryLoader.getEndpoints()) {
                String gatewayPath = "/gateway/" + endpoint.getPath();
                PathItem pathItem = paths.getOrDefault(gatewayPath, new PathItem());
                Operation operation = buildOperation(endpoint);
                switch (endpoint.getMethod().toUpperCase()) {
                    case "GET":
                        pathItem.setGet(operation);
                        break;
                    case "PUT":
                        pathItem.setPut(operation);
                        break;
                    case "PATCH":
                        pathItem.setPatch(operation);
                        break;
                    case "DELETE":
                        pathItem.setDelete(operation);
                        break;
                    default:
                        pathItem.setPost(operation);
                        break;
                }
                paths.addPathItem(gatewayPath, pathItem);
            }
        };
    }

    /**
     * Ensures the {@code EncryptedRequest}/{@code EncryptedResponse} schemas exist under
     * {@code components/schemas} so the {@code $ref}s emitted for each operation resolve.
     */
    private void registerEnvelopeSchemas(OpenAPI openApi) {
        Components components = openApi.getComponents();
        if (components == null) {
            components = new Components();
            openApi.setComponents(components);
        }
        Components target = components;
        ModelConverters.getInstance().readAll(EncryptedRequest.class).forEach(target::addSchemas);
        ModelConverters.getInstance().readAll(EncryptedResponse.class).forEach(target::addSchemas);
    }

    private Operation buildOperation(ApiInventoryLoader.InventoryEndpoint endpoint) {
        String description = ENCRYPTION_NOTE;
        if (!endpoint.getNotes().isBlank()) {
            description += " Notes: " + endpoint.getNotes();
        }
        if (endpoint.isLegacy()) {
            description += " (Deprecated legacy endpoint)";
        }
        if (endpoint.isUnusedInApp()) {
            description += " (Unused in mobile app)";
        }

        Operation operation = new Operation()
                .summary(endpoint.getName())
                .description(description)
                .responses(buildEncryptedResponses());

        if (!"GET".equalsIgnoreCase(endpoint.getMethod())) {
            operation.requestBody(new RequestBody()
                    .required(true)
                    .description(ENCRYPTION_NOTE)
                    .content(new Content().addMediaType(
                            "application/json",
                            new MediaType().schema(new Schema<EncryptedRequest>().$ref(
                                    "#/components/schemas/EncryptedRequest")))));
        } else {
            operation.description(description + " For GET, data/iv/timestamp may be supplied as query parameters.");
        }
        return operation;
    }

    private ApiResponses buildEncryptedResponses() {
        return new ApiResponses().addApiResponse(
                "200",
                new ApiResponse()
                        .description("Encrypted response envelope (HTTP 200 always; decrypt body to inspect result)")
                        .content(new Content().addMediaType(
                                "application/json",
                                new MediaType().schema(new Schema<EncryptedResponse>().$ref(
                                        "#/components/schemas/EncryptedResponse")))));
    }
}
