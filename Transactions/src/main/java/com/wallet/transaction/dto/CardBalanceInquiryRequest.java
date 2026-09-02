package com.wallet.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardBalanceInquiryRequest {
    private String accountNo;
    private String pan;
    private String dateExpiration;
}
