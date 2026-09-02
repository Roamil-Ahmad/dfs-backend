package com.dfs.gateway.config;

import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

/**
 * Cryptographic configuration bound from {@code dfs.crypto.*}.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "dfs.crypto")
public class CryptoProperties {

    private static final int AES_KEY_LENGTH_BYTES = 32;

    private String aesKey;
    private int replayWindowSeconds = 30;

    /**
     * Decoded AES-256 key bytes, populated after startup validation.
     */
    private byte[] decodedAesKey;

    /**
     * Validates and decodes the configured AES key at application startup.
     */
    @PostConstruct
    public void validateAesKey() {
        if (aesKey == null || aesKey.isBlank()) {
            throw new IllegalStateException("dfs.crypto.aes-key must not be blank");
        }
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(aesKey.trim());
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("dfs.crypto.aes-key must be valid Base64", ex);
        }
        if (decoded.length != AES_KEY_LENGTH_BYTES) {
            throw new IllegalStateException(
                    "dfs.crypto.aes-key must decode to exactly 32 bytes, got " + decoded.length);
        }
        this.decodedAesKey = decoded;
    }
}
