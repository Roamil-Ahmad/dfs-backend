package com.dfs.app.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePinRequest {
    private String pan;
    private String relationshipNum;
    private String oldPin;
    private String newPin;
    private String confirmNewPin;
}