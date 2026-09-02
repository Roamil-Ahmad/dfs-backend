package com.dfs.gateway.routing;

import com.dfs.gateway.config.DfsProperties;
import com.dfs.gateway.exception.RouteNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RouteRegistryTest {

    private RouteRegistry routeRegistry;

    @BeforeEach
    void setUp() {
        DfsProperties properties = new DfsProperties();
        properties.setRoutes(List.of(
                RouteDefinition.builder()
                        .pathPrefix("/gateway/transactions")
                        .downstreamBaseUrl("http://tx-service:8080")
                        .stripPrefix("/gateway")
                        .build(),
                RouteDefinition.builder()
                        .pathPrefix("/gateway/bbs-sdks")
                        .downstreamBaseUrl("http://tx-service:8080")
                        .stripPrefix("/gateway")
                        .build(),
                RouteDefinition.builder()
                        .pathPrefix("/gateway")
                        .downstreamBaseUrl("http://app-service:8080")
                        .stripPrefix("/gateway")
                        .build()
        ));
        routeRegistry = new RouteRegistry(properties);
    }

    @Test
    void resolve_appPath_usesBaseService() {
        RouteRegistry.ResolvedRoute route = routeRegistry.resolve("/gateway/app/v1/login");
        assertEquals("http://app-service:8080/app/v1/login", route.getDownstreamUrl());
    }

    @Test
    void resolve_transactionsPath_usesTxService() {
        RouteRegistry.ResolvedRoute route = routeRegistry.resolve("/gateway/transactions/v1/paybill");
        assertEquals("http://tx-service:8080/transactions/v1/paybill", route.getDownstreamUrl());
    }

    @Test
    void resolve_bbsSdkPath_usesTxService() {
        RouteRegistry.ResolvedRoute route = routeRegistry.resolve("/gateway/bbs-sdks/api/match");
        assertEquals("http://tx-service:8080/bbs-sdks/api/match", route.getDownstreamUrl());
    }

    @Test
    void resolve_unknownPath_throwsRouteNotFound() {
        assertThrows(RouteNotFoundException.class, () -> routeRegistry.resolve("/unknown/path"));
    }

    @Test
    void normalizePath_collapsesDuplicateSlashes() {
        assertEquals("/gateway/app/v1/login", RouteRegistry.normalizePath("/gateway//app/v1/login/"));
    }
}
