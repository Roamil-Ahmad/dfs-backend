package com.dfs.agentapp.dto.common;

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
    private String userType;
    private String smsTemplateCode;
    private String identifier;
    

}
