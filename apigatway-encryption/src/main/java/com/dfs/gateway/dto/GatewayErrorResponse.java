package com.dfs.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Plaintext error payload encrypted inside the response envelope.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private String code;
    private String message;
    private String timestamp;
}
