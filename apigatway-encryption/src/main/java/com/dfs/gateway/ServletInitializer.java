package com.dfs.gateway;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Enables deployment of the gateway as a WAR to an external servlet container (e.g. Apache Tomcat).
 *
 * <p>Without this initializer, an external container unpacks the WAR but never bootstraps Spring's
 * {@code DispatcherServlet}, causing every request to return HTTP 404.</p>
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CryptoGatewayApplication.class);
    }
}
