package com.dfs.agentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
