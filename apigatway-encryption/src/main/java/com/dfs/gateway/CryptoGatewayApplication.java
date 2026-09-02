package com.dfs.gateway;

import com.dfs.gateway.config.DfsProperties;
import com.dfs.gateway.config.CryptoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Entry point for the Barkat Pay AES-256-GCM crypto gateway.
 */
@SpringBootApplication
@EnableConfigurationProperties({CryptoProperties.class, DfsProperties.class})
public class CryptoGatewayApplication {

    /**
     * Starts the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CryptoGatewayApplication.class, args);
    }
}
