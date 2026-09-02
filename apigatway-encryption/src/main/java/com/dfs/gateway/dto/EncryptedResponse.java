package com.dfs.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Encrypted response envelope returned to the mobile client.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "EncryptedResponse", description = "AES-256-GCM encrypted response envelope")
public class EncryptedResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String data;
    private String iv;
    private String timestamp;
}
