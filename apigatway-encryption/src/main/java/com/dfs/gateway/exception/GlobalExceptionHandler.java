package com.dfs.gateway.exception;

import com.dfs.gateway.crypto.AesGcmCryptoService;
import com.dfs.gateway.dto.EncryptedResponse;
import com.dfs.gateway.dto.GatewayErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * Global exception handler that always returns HTTP 200 with an encrypted error payload.
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final AesGcmCryptoService aesGcmCryptoService;
    private final ObjectMapper objectMapper;

    /**
     * Handles replay attack detection failures.
     */
    @ExceptionHandler(ReplayAttackException.class)
    public ResponseEntity<EncryptedResponse> handleReplayAttack(ReplayAttackException ex) {
        log.error("Replay attack detected", ex);
        return encryptedError(GatewayErrorCodes.REPLAY_ATTACK_DETECTED, ex.getMessage());
    }

    /**
     * Handles decryption failures.
     */
    @ExceptionHandler(DecryptionException.class)
    public ResponseEntity<EncryptedResponse> handleDecryption(DecryptionException ex) {
        log.error("Decryption failed", ex);
        return encryptedError(GatewayErrorCodes.DECRYPTION_FAILED, ex.getMessage());
    }

    /**
     * Handles route resolution failures.
     */
    @ExceptionHandler(RouteNotFoundException.class)
    public ResponseEntity<EncryptedResponse> handleRouteNotFound(RouteNotFoundException ex) {
        log.error("Route not found", ex);
        return encryptedError(GatewayErrorCodes.ROUTE_NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles downstream service failures.
     */
    @ExceptionHandler(DownstreamException.class)
    public ResponseEntity<EncryptedResponse> handleDownstream(DownstreamException ex) {
        log.error("Downstream error [status={}]: {}", ex.getDownstreamStatus(), ex.getMessage(), ex);
        return encryptedError(ex.getErrorCode(), ex.getMessage());
    }

    /**
     * Handles invalid client requests.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<EncryptedResponse> handleInvalidRequest(IllegalArgumentException ex) {
        log.error("Invalid request", ex);
        return encryptedError(GatewayErrorCodes.INVALID_REQUEST, ex.getMessage());
    }

    /**
     * Handles all other unexpected errors.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<EncryptedResponse> handleGeneric(Exception ex) {
        log.error("Unexpected gateway error", ex);
        return encryptedError(GatewayErrorCodes.INTERNAL_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<EncryptedResponse> encryptedError(String code, String message) {
        GatewayErrorResponse error = GatewayErrorResponse.builder()
                .success(false)
                .code(code)
                .message(message)
                .timestamp(Instant.now().toString())
                .build();
        try {
            String json = objectMapper.writeValueAsString(error);
            EncryptedResponse encrypted = aesGcmCryptoService.encrypt(json);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(encrypted);
        } catch (JsonProcessingException ex) {
            log.error("Failed to serialize error response", ex);
            throw new IllegalStateException("Failed to build encrypted error response", ex);
        }
    }
}
