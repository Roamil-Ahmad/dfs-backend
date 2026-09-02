package com.dfs.gateway.exception;

import lombok.Getter;

/**
 * Thrown when a downstream service call fails or times out.
 */
@Getter
public class DownstreamException extends RuntimeException {

    private final String errorCode;
    private final int downstreamStatus;

    public DownstreamException(String errorCode, String message) {
        this(errorCode, message, 0);
    }

    public DownstreamException(String errorCode, String message, int downstreamStatus) {
        super(message);
        this.errorCode = errorCode;
        this.downstreamStatus = downstreamStatus;
    }

    public DownstreamException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.downstreamStatus = 0;
    }
}
