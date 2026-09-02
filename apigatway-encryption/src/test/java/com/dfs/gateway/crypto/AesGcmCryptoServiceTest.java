package com.dfs.gateway.crypto;

import com.dfs.gateway.config.CryptoProperties;
import com.dfs.gateway.dto.EncryptedResponse;
import com.dfs.gateway.exception.DecryptionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AesGcmCryptoServiceTest {

    private static final String TEST_KEY_BASE64 = "T5wV3hJPn80R8fSunNf6U+gyyThbdAgAghheQZdz5Aw=";

    private AesGcmCryptoService cryptoService;

    @BeforeEach
    void setUp() {
        CryptoProperties properties = new CryptoProperties();
        properties.setAesKey(TEST_KEY_BASE64);
        properties.validateAesKey();
        cryptoService = new AesGcmCryptoService(properties);
    }

    @Test
    void encryptAndDecrypt_roundTrip() {
        String plain = "{\"username\":\"03450000000\",\"password\":\"1234\"}";
        EncryptedResponse encrypted = cryptoService.encrypt(plain);

        assertNotNull(encrypted.getData());
        assertNotNull(encrypted.getIv());
        assertNotNull(encrypted.getTimestamp());

        String decrypted = cryptoService.decrypt(encrypted.getData(), encrypted.getIv());
        assertEquals(plain, decrypted);
    }

    @Test
    void decrypt_withTamperedCiphertext_throwsDecryptionException() {
        EncryptedResponse encrypted = cryptoService.encrypt("{\"ok\":true}");
        byte[] cipherBytes = Base64.getDecoder().decode(encrypted.getData());
        cipherBytes[0] ^= 0xFF;
        String tampered = Base64.getEncoder().encodeToString(cipherBytes);

        assertThrows(DecryptionException.class, () -> cryptoService.decrypt(tampered, encrypted.getIv()));
    }

    @Test
    void validateAesKey_withInvalidLength_failsFast() {
        CryptoProperties properties = new CryptoProperties();
        properties.setAesKey(Base64.getEncoder().encodeToString("short-key".getBytes()));

        IllegalStateException ex = assertThrows(IllegalStateException.class, properties::validateAesKey);
        assertTrue(ex.getMessage().contains("32 bytes"));
    }
}
