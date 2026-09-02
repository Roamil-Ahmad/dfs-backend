package com.dfs.gateway.filter;

import com.dfs.gateway.config.CryptoProperties;
import com.dfs.gateway.exception.ReplayAttackException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * Validates request timestamps to prevent replay attacks.
 */
@Component
@RequiredArgsConstructor
public class ReplayAttackGuard {

    private final CryptoProperties cryptoProperties;

    /**
     * Validates that the request timestamp is within the configured replay window.
     * This check runs before decryption.
     *
     * @param timestamp ISO-8601 UTC timestamp from the request envelope
     * @throws ReplayAttackException when the timestamp is outside the allowed window
     */
    public void validate(String timestamp) {
        if (timestamp == null || timestamp.isBlank()) {
            throw new ReplayAttackException("Request timestamp is missing");
        }
        Instant requestInstant;
        try {
            requestInstant = Instant.parse(timestamp.trim());
        } catch (Exception ex) {
            throw new ReplayAttackException("Request timestamp is not valid ISO-8601 UTC");
        }
        long skewSeconds = Math.abs(Duration.between(requestInstant, Instant.now()).getSeconds());
        if (skewSeconds > cryptoProperties.getReplayWindowSeconds()) {
            throw new ReplayAttackException(
                    "Request timestamp is outside the allowed replay window of "
                            + cryptoProperties.getReplayWindowSeconds() + " seconds");
        }
    }
}
