package com.dfs.cardapi.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateRequest {
    private String pan;
    private String pin;
    private String cvv;
    private String track2;
}
