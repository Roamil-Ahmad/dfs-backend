package com.dfs.thirdparties.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateOtpResponse {
    private String mobileNumber;
    private String otpId;
    private String otpCode;
    private String email;
    private String expiryDateTime;
    private String status;

}
