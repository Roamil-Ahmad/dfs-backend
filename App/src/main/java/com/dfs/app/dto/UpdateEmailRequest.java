package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateEmailRequest {
    private String mobileNumber;
    private String email;
    private String otp;
}
