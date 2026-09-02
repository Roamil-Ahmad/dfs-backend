package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwoFactorAuthRequest {
    private String email;
    private String mobileNo;
    private String twoFaEnabled;
    private String twoFaType;
}
