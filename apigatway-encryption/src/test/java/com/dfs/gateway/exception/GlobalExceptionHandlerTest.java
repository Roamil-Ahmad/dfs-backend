package com.dfs.gateway.exception;

import com.dfs.gateway.config.CryptoProperties;
import com.dfs.gateway.crypto.AesGcmCryptoService;
import com.dfs.gateway.dto.EncryptedResponse;
import com.dfs.gateway.dto.GatewayErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private AesGcmCryptoService cryptoService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        CryptoProperties properties = new CryptoProperties();
        properties.setAesKey("T5wV3hJPn80R8fSunNf6U+gyyThbdAgAghheQZdz5Aw=");
        properties.validateAesKey();
        cryptoService = new AesGcmCryptoService(properties);
        objectMapper = new ObjectMapper();
        handler = new GlobalExceptionHandler(cryptoService, objectMapper);
    }

    @Test
    void handleDecryption_returnsHttp200WithEncryptedError() throws Exception {
        ResponseEntity<EncryptedResponse> response =
                handler.handleDecryption(new DecryptionException("bad ciphertext"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        EncryptedResponse body = response.getBody();
        assertEquals(
                GatewayErrorCodes.DECRYPTION_FAILED,
                readErrorCode(body));
    }

    @Test
    void handleReplayAttack_returnsEncryptedReplayCode() throws Exception {
        ResponseEntity<EncryptedResponse> response =
                handler.handleReplayAttack(new ReplayAttackException("stale timestamp"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(
                GatewayErrorCodes.REPLAY_ATTACK_DETECTED,
                readErrorCode(response.getBody()));
    }

    private String readErrorCode(EncryptedResponse encrypted) throws Exception {
        String json = cryptoService.decrypt(encrypted.getData(), encrypted.getIv());
        GatewayErrorResponse error = objectMapper.readValue(json, GatewayErrorResponse.class);
        assertFalse(error.isSuccess());
        return error.getCode();
    }
}
