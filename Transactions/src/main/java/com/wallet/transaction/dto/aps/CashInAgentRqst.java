package com.wallet.transaction.dto.aps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CashInAgentRqst {

    private String pan;
    private String merchantType;
    private String amount;
    private String tract2Data;
    private String pin;
    private String stan;
    private String rrn;
    private String cardAcceptorIdentification;
    private String cardAcceptorCode;

    private PointOfService pointOfService;
    private CardAcceptorNameAndLocation cardAcceptorNameAndLocation;

    private String cvcPresent;
    private String cvc2;
    private String card2Expiration; // e.g. 2608
    private String transactionType;
    private String terminalType;
    private String card2Number;
    private String currencyCodeTransaction;
    private String currencyCodeCardBilling;
}
