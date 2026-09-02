package com.dfs.backoffice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String otpType;
    private String email;
    private String expiryDateTime;
    private String status;

}
