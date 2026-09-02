package com.dfs.gateway.exception;

/**
 * Thrown when no route definition matches the incoming gateway path.
 */
public class RouteNotFoundException extends RuntimeException {

    public RouteNotFoundException(String message) {
        super(message);
    }
}
