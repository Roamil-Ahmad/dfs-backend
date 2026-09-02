package com.dfs.gateway.config;

import com.dfs.gateway.routing.RouteDefinition;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Root Barkat Pay gateway configuration including downstream URLs and route table.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "dfs")
public class DfsProperties {

    private Downstream downstream = new Downstream();
    private List<RouteDefinition> routes = new ArrayList<>();

    /**
     * Downstream service base URL configuration.
     */
    @Getter
    @Setter
    public static class Downstream {
        private String baseUrl;
        private String dcUrl;
    }
}
