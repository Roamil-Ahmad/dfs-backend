package com.dfs.app.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPinRequest {
    private String pan;
    private String relationshipNum;
    private String pin;
    private String confirmPin;
}