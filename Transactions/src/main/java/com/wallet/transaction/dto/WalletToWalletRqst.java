package com.wallet.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class WalletToWalletRqst {
    private String accountNo;
    private String pan;
    private String mpin;
    private String amount;
    private BigDecimal appUserId;
    private String stan;
    private String rrn;
    private String toBankName;
}
