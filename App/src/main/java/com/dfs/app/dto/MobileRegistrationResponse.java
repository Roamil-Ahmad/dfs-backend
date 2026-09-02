package com.dfs.app.dto;

import com.dfs.app.dto.common.GenerateOtpResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MobileRegistrationResponse {
    private String authToken;
    private GenerateOtpResponse generateOtpResponse;
}
