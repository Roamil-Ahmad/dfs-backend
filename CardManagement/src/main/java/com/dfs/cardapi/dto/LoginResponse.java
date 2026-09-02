package com.dfs.cardapi.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private int responseCode;
    private String responseMessage;
    private LoginResponseBody responseBody;
}