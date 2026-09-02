package com.dfs.gateway.filter;

import com.dfs.gateway.config.CryptoProperties;
import com.dfs.gateway.exception.ReplayAttackException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReplayAttackGuardTest {

    private ReplayAttackGuard replayAttackGuard;

    @BeforeEach
    void setUp() {
        CryptoProperties properties = new CryptoProperties();
        properties.setAesKey("T5wV3hJPn80R8fSunNf6U+gyyThbdAgAghheQZdz5Aw=");
        properties.setReplayWindowSeconds(30);
        properties.validateAesKey();
        replayAttackGuard = new ReplayAttackGuard(properties);
    }

    @Test
    void validate_withCurrentTimestamp_succeeds() {
        assertDoesNotThrow(() -> replayAttackGuard.validate(Instant.now().toString()));
    }

    @Test
    void validate_withExpiredTimestamp_throwsReplayAttackException() {
        String expired = Instant.now().minusSeconds(120).toString();
        assertThrows(ReplayAttackException.class, () -> replayAttackGuard.validate(expired));
    }

    @Test
    void validate_withFutureTimestamp_throwsReplayAttackException() {
        String future = Instant.now().plusSeconds(120).toString();
        assertThrows(ReplayAttackException.class, () -> replayAttackGuard.validate(future));
    }

    @Test
    void validate_withMissingTimestamp_throwsReplayAttackException() {
        assertThrows(ReplayAttackException.class, () -> replayAttackGuard.validate(null));
    }
}
