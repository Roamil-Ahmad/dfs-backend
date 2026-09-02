package com.dfs.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenerateNotificationRequest {
 


    private String mobileNumber;

    private String type;

    private String sms;

    private String email;

    private String subject;

    private BigDecimal templateId;




}
