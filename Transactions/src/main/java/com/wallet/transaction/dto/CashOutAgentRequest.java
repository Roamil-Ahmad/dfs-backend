package com.wallet.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CashOutAgentRequest {
    private String accountNo;
    private String pan;
    private String amount;
    private String pin;
    private String cvc2;
    private String card2Expiration;
    private String rrn;
    private String otpPin;

}
