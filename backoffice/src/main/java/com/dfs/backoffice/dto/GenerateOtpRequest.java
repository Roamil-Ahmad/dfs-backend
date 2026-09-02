package com.dfs.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenerateOtpRequest {
    private String otpType;
    private String mobileNumber;
    private String email;
    private String otpTemplateCode;
    private String otpIdentifier;
}
