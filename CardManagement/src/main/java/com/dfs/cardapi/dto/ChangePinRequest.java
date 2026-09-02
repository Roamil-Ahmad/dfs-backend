package com.dfs.cardapi.dto;

import lombok.*;

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