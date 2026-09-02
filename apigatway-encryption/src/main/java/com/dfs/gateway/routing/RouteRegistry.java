package com.dfs.gateway.routing;

import com.dfs.gateway.config.DfsProperties;
import com.dfs.gateway.exception.RouteNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Comparator;
import java.util.List;

/**
 * Resolves incoming gateway paths to downstream target URLs using longest-prefix matching.
 */
@Component
@RequiredArgsConstructor
public class RouteRegistry {

    private final DfsProperties dfsProperties;

    /**
     * Resolved downstream target for a gateway request.
     */
    @Getter
    public static class ResolvedRoute {
        private final String downstreamUrl;
        private final RouteDefinition routeDefinition;

        public ResolvedRoute(String downstreamUrl, RouteDefinition routeDefinition) {
            this.downstreamUrl = downstreamUrl;
            this.routeDefinition = routeDefinition;
        }
    }

    /**
     * Resolves the downstream URL for the given gateway request path.
     *
     * @param requestPath full servlet path (e.g. {@code /gateway/app/v1/login})
     * @return resolved downstream URL
     * @throws RouteNotFoundException when no route prefix matches
     */
    public ResolvedRoute resolve(String requestPath) {
        if (requestPath == null || requestPath.isBlank()) {
            throw new RouteNotFoundException("Request path is empty");
        }
        String normalizedPath = normalizePath(requestPath);
        List<RouteDefinition> routes = dfsProperties.getRoutes();
        if (routes == null || routes.isEmpty()) {
            throw new RouteNotFoundException("No routes configured");
        }

        RouteDefinition matched = routes.stream()
                .filter(route -> normalizedPath.startsWith(normalizePath(route.getPathPrefix())))
                .max(Comparator.comparingInt(route -> normalizePath(route.getPathPrefix()).length()))
                .orElseThrow(() -> new RouteNotFoundException("No route matched for path: " + normalizedPath));

        String stripPrefix = normalizePath(matched.getStripPrefix());
        String remainder = normalizedPath.substring(stripPrefix.length());
        if (remainder.isEmpty()) {
            remainder = "/";
        } else if (!remainder.startsWith("/")) {
            remainder = "/" + remainder;
        }

        String baseUrl = trimTrailingSlash(matched.getDownstreamBaseUrl());
        String downstreamPath = remainder.startsWith("/") ? remainder.substring(1) : remainder;
        String downstreamUrl = downstreamPath.isEmpty()
                ? baseUrl + "/"
                : baseUrl + "/" + downstreamPath;

        return new ResolvedRoute(downstreamUrl, matched);
    }

    /**
     * Normalizes a path by collapsing duplicate slashes and trimming trailing slashes.
     *
     * @param path raw path
     * @return normalized path
     */
    public static String normalizePath(String path) {
        if (path == null || path.isBlank()) {
            return "/";
        }
        String normalized = path.trim().replaceAll("/+", "/");
        if (normalized.length() > 1 && normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        return normalized;
    }

    private String trimTrailingSlash(String url) {
        if (url == null || url.isBlank()) {
            throw new RouteNotFoundException("Downstream base URL is not configured");
        }
        String trimmed = url.trim();
        while (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        URI.create(trimmed);
        return trimmed;
    }
}
