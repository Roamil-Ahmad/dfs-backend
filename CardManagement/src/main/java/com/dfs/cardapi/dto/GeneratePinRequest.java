package com.dfs.cardapi.dto;

import lombok.*;

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
