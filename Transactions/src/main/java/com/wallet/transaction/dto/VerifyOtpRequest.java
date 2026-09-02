package com.wallet.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOtpRequest {
    private String otpId;
    private String otpPin;
    private String otpType;
    private String userType;
    private String mobileNumber;
    private String email;
}
