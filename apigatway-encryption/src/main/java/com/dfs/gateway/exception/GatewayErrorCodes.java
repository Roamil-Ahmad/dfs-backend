package com.dfs.gateway.exception;

/**
 * Standard gateway error codes returned inside encrypted error payloads.
 */
public final class GatewayErrorCodes {

    public static final String DECRYPTION_FAILED = "DECRYPTION_FAILED";
    public static final String REPLAY_ATTACK_DETECTED = "REPLAY_ATTACK_DETECTED";
    public static final String INVALID_REQUEST = "INVALID_REQUEST";
    public static final String DOWNSTREAM_ERROR = "DOWNSTREAM_ERROR";
    public static final String DOWNSTREAM_TIMEOUT = "DOWNSTREAM_TIMEOUT";
    public static final String ROUTE_NOT_FOUND = "ROUTE_NOT_FOUND";
    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";

    private GatewayErrorCodes() {
    }
}
