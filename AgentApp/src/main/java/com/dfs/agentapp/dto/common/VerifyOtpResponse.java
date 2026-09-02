package com.dfs.agentapp.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOtpResponse {
    private String tries;
    private String otpId;
    private String status;
}
