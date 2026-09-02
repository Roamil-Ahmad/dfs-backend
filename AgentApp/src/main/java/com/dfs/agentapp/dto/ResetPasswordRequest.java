package com.dfs.agentapp.dto;

import com.dfs.agentapp.dto.common.VerifyOtpRequest;
import com.dfs.agentapp.dto.common.VerifyOtpResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPasswordRequest {
    private VerifyOtpRequest verifyOtpRequest;
    private VerifyOtpResponse verifyOtpResponse;
    private String newPassword;
    private String confirmPassword;
    private String mobileNumber;
    private String step;
}
