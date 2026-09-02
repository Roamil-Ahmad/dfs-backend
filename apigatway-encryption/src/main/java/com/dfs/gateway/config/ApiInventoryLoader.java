package com.dfs.gateway.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads and exposes API endpoint metadata from {@code api-inventory.json}.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiInventoryLoader {

    private final ObjectMapper objectMapper;

    @Getter
    private final List<InventoryEndpoint> endpoints = new ArrayList<>();

    /**
     * Loads the inventory file from the classpath at startup.
     */
    @PostConstruct
    public void loadInventory() {
        try (InputStream inputStream = new ClassPathResource("api-inventory.json").getInputStream()) {
            JsonNode root = objectMapper.readTree(inputStream);
            JsonNode endpointNodes = root.path("endpoints");
            Map<String, InventoryEndpoint> unique = new LinkedHashMap<>();
            for (JsonNode node : endpointNodes) {
                String method = node.path("method").asText("POST");
                String path = normalizeInventoryPath(node.path("path").asText());
                String key = method + ":" + path;
                if (unique.containsKey(key)) {
                    continue;
                }
                InventoryEndpoint endpoint = new InventoryEndpoint(
                        node.path("id").asInt(),
                        node.path("name").asText(),
                        method,
                        path,
                        node.path("notes").asText(""),
                        node.path("legacy").asBoolean(false),
                        node.path("unused_in_app").asBoolean(false)
                );
                unique.put(key, endpoint);
            }
            endpoints.addAll(unique.values());
            log.info("Loaded {} unique API inventory endpoints for documentation", endpoints.size());
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to load api-inventory.json from classpath", ex);
        }
    }

    private String normalizeInventoryPath(String path) {
        if (path == null || path.isBlank()) {
            return "";
        }
        String normalized = path.trim().replaceAll("/+", "/");
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        return normalized;
    }

    /**
     * Endpoint metadata from the Android API inventory.
     */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InventoryEndpoint {
        private final int id;
        private final String name;
        private final String method;
        private final String path;
        private final String notes;
        private final boolean legacy;
        private final boolean unusedInApp;

        public InventoryEndpoint(
                int id,
                String name,
                String method,
                String path,
                String notes,
                boolean legacy,
                boolean unusedInApp) {
            this.id = id;
            this.name = name;
            this.method = method;
            this.path = path;
            this.notes = notes;
            this.legacy = legacy;
            this.unusedInApp = unusedInApp;
        }
    }
}
