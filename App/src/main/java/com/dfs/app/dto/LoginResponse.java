package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {
    private long customerId;
    private String accountTitle;
    private String accountTitleDari;
    private long accountId;
    private String accountNo;
    private BigDecimal currentBalance;
    private BigDecimal dailyCrLmt;
    private BigDecimal dailyDrLmt;
    private BigDecimal monthlyCrLmt;
    private BigDecimal monthlyDrLmt;
    private BigDecimal yearlyCrLmt;
    private BigDecimal yearlyDrLmt;
    private String levelDescr;
    private String token;
    private String accountType;
    private String accountStatusDescr;
    private Long accountStatusId;
    private Long loginHistoryId;
    private String biometricallyVerified;
    private String qr;
    private String iban;
    private String nidNo;
    private Long appUserId;
    private String dfsId;
    private String accountLevelCode;
    private String fatherName;
    private String grandFatherName;
    private String username;
    private String selfie;
    private String autoLoginToken;
    private String email;
    private String twoFAEnabled;
    private String customerRiskProfile;
    private Object expiry;


}
