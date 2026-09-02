package com.dfs.agentapp.dto;

import com.dfs.agentapp.dto.common.VerifyOtpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerifyDeviceRequest {
    private VerifyOtpRequest verifyOtpRequest;
    private String appVersion;
    private String mobileNumber;
    private String deviceModel;
    private String imeiNo;
    private String ipAddressP;
    private String ipAddressA;
}
