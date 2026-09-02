package com.dfs.gateway.routing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Route definition mapping a gateway path prefix to a downstream base URL.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pathPrefix;
    private String downstreamBaseUrl;
    private String stripPrefix;
}
