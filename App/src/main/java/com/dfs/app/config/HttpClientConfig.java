package com.dfs.app.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * Shared, Spring-managed HTTP clients WITH connect/read timeouts, so a slow or unreachable
 * downstream cannot hold a Tomcat worker thread (and eventually exhaust the pool) indefinitely.
 *
 * Configured centrally here — HelperClass does not change:
 *  - RestTemplate bean (used by the Google-auth call) gets connect + read timeouts.
 *  - WebClientCustomizer applies a Reactor Netty connector with timeouts to the auto-configured
 *    WebClient.Builder that HelperClass injects, so every WebClient call inherits them.
 *
 * Spring Cloud Sleuth still instruments both (it composes with this), so traceId keeps propagating.
 */
@Configuration
public class HttpClientConfig {

    @Value("${http.client.connect-timeout-ms:3000}")
    private int connectTimeoutMs;

    @Value("${http.client.read-timeout-ms:10000}")
    private int readTimeoutMs;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(connectTimeoutMs))
                .setReadTimeout(Duration.ofMillis(readTimeoutMs))
                .build();
    }

    @Bean
    public WebClientCustomizer webClientTimeoutCustomizer() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs) // TCP connect
                .responseTimeout(Duration.ofMillis(readTimeoutMs));             // overall response wait
        return builder -> builder.clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
