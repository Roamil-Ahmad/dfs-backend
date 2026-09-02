package com.wallet.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardToCardRequest {
    private String accountNo;
    private String fromPan;
    private String toPan;
    private String amount;
    private String bankId;
    private String nickName;
    private String fromCardTitle;
    private String toCardTitle;
    private String mpin;
    private String otpPin;
    private String rrn;
    private String expiry;
    private String cvc;
}
