package com.wallet.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class WalletToWalletIssuerRequest {
    private String fromAccountNo;
    private String toAccountNo;
    private String amount;
    private BigDecimal appUserId;
    private String clientSecret;
    private String channelCode;
    private String stan;
    private String rrn;
}
