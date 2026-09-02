package com.dfs.cardapi.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
@Component
public class AESencryption {

    private static final String WARNING_CODE = JwtConstants.JWT_SUPRESS_WARNING;
    private byte[] ivParameter = JwtConstants.IV_PARAM.getBytes(StandardCharsets.UTF_8);

    @SuppressWarnings(WARNING_CODE)
    public String encryptwith256( String strToEncrypt) {
        try {
            SecretKeySpec secretKey = generateSecretKey(JwtConstants.AES_SECRET_KEY);
            Cipher cipher = Cipher.getInstance(JwtConstants.PADDING_CBC);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ivParameter));
            byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings(WARNING_CODE)
    public String decrypt(String strToDecrypt) {
        try {
            SecretKeySpec secretKey = generateSecretKey(JwtConstants.AES_SECRET_KEY);
            Cipher cipher = Cipher.getInstance(JwtConstants.PADDING_CBC);
            // Generate a random IV
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ivParameter));
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private IvParameterSpec GenerateARandomIV() {
        byte[] iv = new byte[16]; // AES block size is 16 bytes
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        return ivSpec;
    }

    private SecretKeySpec generateSecretKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, JwtConstants.ALGORITHM);
    }
}
