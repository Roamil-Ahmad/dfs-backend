package com.wallet.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayBillCpRequest {
    private String accountNo;
    private String pan;
    private String cardTitle;
    private String amount;
    private String pin;
    private String billerName;
    private String serviceId;
    private String billRefNo;
    private String billAmount;
    private String expiry;
    private String cvv;
    private String requestId;
    private String otpPin;
    private String rrn;
    private String track2data;
}
