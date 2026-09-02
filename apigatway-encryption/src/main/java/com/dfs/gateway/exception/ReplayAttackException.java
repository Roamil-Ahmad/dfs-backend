package com.dfs.gateway.exception;

/**
 * Thrown when the request timestamp is outside the allowed replay window.
 */
public class ReplayAttackException extends RuntimeException {

    public ReplayAttackException(String message) {
        super(message);
    }
}
