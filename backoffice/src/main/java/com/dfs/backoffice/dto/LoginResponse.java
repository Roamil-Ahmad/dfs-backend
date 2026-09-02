package com.dfs.backoffice.dto;

import com.dfs.backoffice.model.TblAppUser;
import com.dfs.backoffice.model.TblAppUserLoginHistory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private String twoFA;
    private TblAppUser tblAppUser;
    private String errorResponse;
    private String sessionExpireDuration;
    private TblAppUserLoginHistory tblAppUserLoginHistory;
    private List<MenuResponse> menu = new ArrayList<>();
    private GenerateOtpResponse otpResponse;
}
