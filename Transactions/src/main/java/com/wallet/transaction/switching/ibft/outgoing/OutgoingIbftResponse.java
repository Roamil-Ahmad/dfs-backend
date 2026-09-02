package com.wallet.transaction.switching.ibft.outgoing;

import lombok.Data;
import lombok.NoArgsConstructor;

/** Result of an outgoing IBFT returned to the DFS application. */
@Data
@NoArgsConstructor
public class OutgoingIbftResponse {

    private String fromAccountNo;
    private String beneficiaryAccountNo;
    private String beneficiaryAccountTitle;
    private String beneficiaryBankName;
    private String beneficiaryBankImd;
    private String amount;
    private String transactionReference;

    /** DE-11 and DE-37 - kept so the caller can reconcile and query later. */
    private String stan;
    private String rrn;
    /** DE-38 authorisation id assigned by the core. */
    private String authIdResponse;

    /** Identity of the posted transaction, PKG_MW P_TRANS_HEAD_ID. */
    private Long transactionId;

    /** ISO 8583 DE-39 answered by the switch. */
    private String switchResponseCode;
    /** Outcome reported by PKG_MW.OUTGOING_IBFT. */
    private String coreResponseCode;
    private String responseDescription;
}
