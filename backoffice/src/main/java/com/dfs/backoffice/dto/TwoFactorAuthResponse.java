package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwoFactorAuthResponse {
    private String twoFaEnabled;
    private String twoFaType;
    private String mobileNo;
    private String email;
}
