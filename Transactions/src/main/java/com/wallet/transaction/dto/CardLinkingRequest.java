package com.wallet.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardLinkingRequest {
    private String pan;
    private String accountNo;
    private String mpin;
    private long bankId;
    private long currencyId;
    private String cardDisplayName;
    private String cvv;
    private String expiryDate;

}
