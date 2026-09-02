package com.dfs.gateway.exception;

/**
 * Thrown when AES-256-GCM decryption fails or authentication tag verification fails.
 */
public class DecryptionException extends RuntimeException {

    public DecryptionException(String message) {
        super(message);
    }

    public DecryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
