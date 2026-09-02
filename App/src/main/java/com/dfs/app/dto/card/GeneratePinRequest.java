package com.dfs.app.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for external card API generate-pin (G = Generate, F = Forgot).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratePinRequest {
    private String pan;
    private String relationshipNum;
    private String pin;
    private String confirmPin;
    /** G = Generate, F = Forgot */
    private String flag;
}
