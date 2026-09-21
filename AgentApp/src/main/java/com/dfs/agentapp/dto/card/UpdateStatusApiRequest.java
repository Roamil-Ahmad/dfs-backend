package com.dfs.agentapp.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body sent to external card API for update-status (pan and statusCode only).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusApiRequest {
    private String pan;
    private String statusCode;
}
