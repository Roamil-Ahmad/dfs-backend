package com.dfs.agentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Binds the calling user's handset to their own app user.
 *
 * <p>Carries no username or mobile number on purpose: the user is taken from the bearer token, so
 * a caller cannot register a device against somebody else by naming them.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDeviceRequest {
    private String imeiNo;
    private String deviceModel;
    private String appVersion;
    private String macAddress;

}
