package com.wallet.transaction.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CashInAgentRequest {
    private String accountNo;
    private String pan;
    private String amount;
    private String pin;
    private String accountTitle;

}
