package com.dfs.gateway.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Apache HttpClient5 configuration for downstream forwarding.
 */
@Configuration
public class HttpClientConfig {

    private static final int MAX_TOTAL_CONNECTIONS = 200;
    private static final int MAX_CONNECTIONS_PER_ROUTE = 50;
    private static final Timeout CONNECT_TIMEOUT = Timeout.ofSeconds(5);
    private static final Timeout RESPONSE_TIMEOUT = Timeout.ofSeconds(30);

    /**
     * Provides a pooled {@link CloseableHttpClient} for downstream service calls.
     *
     * @return configured HTTP client
     */
    @Bean(destroyMethod = "close")
    public CloseableHttpClient closeableHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        connectionManager.setDefaultMaxPerRoute(MAX_CONNECTIONS_PER_ROUTE);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(CONNECT_TIMEOUT)
                .setResponseTimeout(RESPONSE_TIMEOUT)
                .build();

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}
