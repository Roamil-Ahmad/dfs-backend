package com.dfs.switchgateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Guards the removals the DFS refactor mandated.
 *
 * The previous architecture carried a retry loop, store-and-forward, ActiveMQ, Elasticsearch
 * logging over HTTP and an outbound proxy. None of it belongs here, and a well-meaning future
 * change could quietly bring one back. These tests fail if that happens.
 */
class RemovedComponentsTest {

    private static final Path SOURCE_ROOT = Paths.get("src", "main", "java");
    private static final Path PROPERTIES = Paths.get("src", "main", "resources", "application.properties");

    private List<Path> sourceFiles() throws IOException {
        try (Stream<Path> files = Files.walk(SOURCE_ROOT)) {
            return files.filter(path -> path.toString().endsWith(".java")).collect(Collectors.toList());
        }
    }

    private String allSources() throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Path file : sourceFiles()) {
            builder.append(Files.readString(file, StandardCharsets.UTF_8)).append('\n');
        }
        return builder.toString();
    }

    @Test
    @DisplayName("store-and-forward is gone: no TblSwitchSaf entity, repository or SAF service")
    void noStoreAndForward() throws IOException {
        String sources = allSources();
        assertThat(sources).doesNotContain("TblSwitchSaf");
        assertThat(sources).doesNotContain("TBLSwitchSaf");
        assertThat(sources).doesNotContain("SafConfig");
        assertThat(sources).doesNotContain("safKafka");
    }

    @Test
    @DisplayName("message queues are gone: no ActiveMQ, no JMS listener, no queue framework")
    void noMessageQueues() throws IOException {
        String sources = allSources();
        assertThat(sources).doesNotContain("JmsListener");
        assertThat(sources).doesNotContain("ActiveMq");
        assertThat(sources).doesNotContain("AmqProducer");
        assertThat(sources).doesNotContain("OutgoingProducer");
        assertThat(sources).doesNotContain("IncomingProducer");
    }

    @Test
    @DisplayName("Elasticsearch logging over HTTP is gone; logging goes through slf4j")
    void noElasticLogging() throws IOException {
        String sources = allSources();
        assertThat(sources).doesNotContain("KafkaELKService");
        assertThat(sources).doesNotContain("ELKPayloadDTO");
        assertThat(sources).doesNotContain("elasticsearch");
        assertThat(sources).contains("org.slf4j.Logger");
    }

    @Test
    @DisplayName("the outbound proxy is gone: every service runs on the same host")
    void noProxy() throws IOException {
        String sources = allSources();
        assertThat(sources).doesNotContain("java.net.Proxy");
        assertThat(sources).doesNotContain("setProxy");
    }

    @Test
    @DisplayName("no transaction is ever replayed: the only retry left is TCP reconnection")
    void noTransactionRetry() throws IOException {
        for (Path file : sourceFiles()) {
            String name = file.getFileName().toString();
            if (name.equals("ReconnectionFilter.java")) {
                continue; // transport-level reconnect, not a transaction retry
            }
            String content = Files.readString(file, StandardCharsets.UTF_8);
            // Comments explaining the absence are fine; executable retry constructs are not.
            assertThat(content)
                    .as("retry construct in %s", name)
                    .doesNotContain("@Retryable")
                    .doesNotContain("RetryTemplate")
                    .doesNotContain("noOfRetries");
        }
    }

    @Test
    @DisplayName("no Zindigi or JS Bank naming survives")
    void noLegacyBranding() throws IOException {
        String sources = allSources().toLowerCase();
        assertThat(sources).doesNotContain("zindigi");
        assertThat(sources).doesNotContain("jsbank");
        assertThat(sources).doesNotContain("jsbldefaults");
    }

    @Test
    @DisplayName("configuration holds no proxy, Elastic, SAF, retry or datasource settings")
    void configurationIsClean() throws IOException {
        String properties = Files.readString(PROPERTIES, StandardCharsets.UTF_8);
        assertThat(properties).doesNotContain("proxy.");
        assertThat(properties).doesNotContain("elasticsearch");
        assertThat(properties).doesNotContain("kafka");
        assertThat(properties).doesNotContain("saf.");
        assertThat(properties).doesNotContain("retry.");
        assertThat(properties).doesNotContain("spring.datasource");
    }

    @Test
    @DisplayName("the simulator endpoint is configured, never hardcoded in Java")
    void simulatorEndpointComesFromConfiguration() throws IOException {
        String properties = Files.readString(PROPERTIES, StandardCharsets.UTF_8);
        assertThat(properties).contains("switch.host");
        assertThat(properties).contains("switch.port");

        String sources = allSources();
        assertThat(sources).contains("${switch.host}");
        assertThat(sources).contains("${switch.port}");
    }
}
