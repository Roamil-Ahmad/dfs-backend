package com.dfs.app.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {
    private String username;
    private String password;
    private String accountTypeCode;
    private String accountRegTypeCode;
    private String fireBaseToken;

}
