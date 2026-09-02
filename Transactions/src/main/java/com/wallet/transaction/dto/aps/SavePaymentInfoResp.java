package com.wallet.transaction.dto.aps;

import com.wallet.transaction.dto.LovResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SavePaymentInfoResp {

    private String stan;
    private String timeLocalTransaction;

    private String retrievalReferenceNumber;

    private String cardAcceptorTerminalIdentification;

    private String responseCode;

    private String additionalDataPrivate;
    private String billNumber;
    private String billAmount;
    private String companyCode;
    private String billPayerName;
    private String billPaymentRequestId;
    private String availableBalance;
    private String currencyAvailable;
    private String fee;
    List<LovResponse> transPurposes;




}
