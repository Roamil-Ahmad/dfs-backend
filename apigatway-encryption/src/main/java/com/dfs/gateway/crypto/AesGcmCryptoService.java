package com.dfs.gateway.crypto;

import com.dfs.gateway.config.CryptoProperties;
import com.dfs.gateway.dto.EncryptedResponse;
import com.dfs.gateway.exception.DecryptionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

/**
 * AES-256-GCM encryption and decryption for gateway envelopes.
 */
@Service
@RequiredArgsConstructor
public class AesGcmCryptoService {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LENGTH_BYTES = 12;
    private static final int GCM_TAG_LENGTH_BITS = 128;

    private final CryptoProperties cryptoProperties;
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Decrypts a Base64-encoded AES-256-GCM ciphertext using the provided IV.
     *
     * @param base64Data Base64 ciphertext including authentication tag
     * @param base64Iv   Base64 12-byte initialization vector
     * @return decrypted plaintext JSON string
     * @throws DecryptionException when decryption or authentication fails
     */
    public String decrypt(String base64Data, String base64Iv) {
        validateCipherInputs(base64Data, base64Iv);
        try {
            byte[] cipherBytes = Base64.getDecoder().decode(base64Data.trim());
            byte[] ivBytes = Base64.getDecoder().decode(base64Iv.trim());
            if (ivBytes.length != IV_LENGTH_BYTES) {
                throw new DecryptionException("IV must decode to exactly 12 bytes");
            }
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, ivBytes);
            SecretKeySpec keySpec = new SecretKeySpec(cryptoProperties.getDecodedAesKey(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);
            byte[] plainBytes = cipher.doFinal(cipherBytes);
            return new String(plainBytes, StandardCharsets.UTF_8);
        } catch (AEADBadTagException ex) {
            throw new DecryptionException("Authentication tag verification failed — payload may be tampered", ex);
        } catch (DecryptionException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DecryptionException("Failed to decrypt request payload", ex);
        }
    }

    /**
     * Encrypts a plaintext JSON string into a response envelope with a fresh IV.
     *
     * @param plainJson plaintext JSON to encrypt
     * @return encrypted response envelope
     */
    public EncryptedResponse encrypt(String plainJson) {
        if (plainJson == null) {
            throw new IllegalArgumentException("plainJson must not be null");
        }
        try {
            byte[] ivBytes = new byte[IV_LENGTH_BYTES];
            secureRandom.nextBytes(ivBytes);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, ivBytes);
            SecretKeySpec keySpec = new SecretKeySpec(cryptoProperties.getDecodedAesKey(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);
            byte[] cipherBytes = cipher.doFinal(plainJson.getBytes(StandardCharsets.UTF_8));
            return EncryptedResponse.builder()
                    .data(Base64.getEncoder().encodeToString(cipherBytes))
                    .iv(Base64.getEncoder().encodeToString(ivBytes))
                    .timestamp(Instant.now().toString())
                    .build();
        } catch (Exception ex) {
            throw new DecryptionException("Failed to encrypt response payload", ex);
        }
    }

    private void validateCipherInputs(String base64Data, String base64Iv) {
        if (base64Data == null || base64Data.isBlank()) {
            throw new IllegalArgumentException("data must not be blank");
        }
        if (base64Iv == null || base64Iv.isBlank()) {
            throw new IllegalArgumentException("iv must not be blank");
        }
    }
}
