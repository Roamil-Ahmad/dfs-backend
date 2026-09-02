package com.wallet.transaction.switching.ibft.outgoing;

import lombok.Data;
import lombok.NoArgsConstructor;

/** Beneficiary details returned to the DFS application before it confirms a transfer. */
@Data
@NoArgsConstructor
public class OutgoingIbftTitleFetchResponse {

    private String beneficiaryAccountNo;
    private String beneficiaryAccountTitle;
    private String beneficiaryBankName;
    private String beneficiaryBankImd;
    private String beneficiaryId;
    private String amount;
    private String stan;
    private String rrn;
    /** ISO 8583 DE-39 as answered by the switch. */
    private String responseCode;
    private String responseDescription;
}
