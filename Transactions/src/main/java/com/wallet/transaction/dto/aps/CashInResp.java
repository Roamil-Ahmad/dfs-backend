package com.wallet.transaction.dto.aps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CashInResp {
    private String pan;
    private String processingCode;
    private String amountTransaction;
    private String stan;
    private String timeLocalTransaction;
    private String dateSettlement;
    private String acquiringInstitutionIdentCode;
    private String retrievalReferenceNumber;
    private String authorizationIdentificationResponse;
    private String responseCode;
    private String cardAcceptorTerminalIdentification;
    private String additionalDataPrivate;
    private String currencyCodeTransaction;
    private String transactionType;
    private String currentBalance;
    private String additionalAmount;
}
