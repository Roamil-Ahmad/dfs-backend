package com.wallet.transaction.switching.billpayment;

import lombok.Data;
import lombok.NoArgsConstructor;

/** Result of a bill payment returned to the DFS application. */
@Data
@NoArgsConstructor
public class BillPaymentResponse {

    private String fromAccountNo;
    private String utilityCompanyCode;
    private String utilityCompanyName;
    private String consumerNo;
    private String amount;
    private String billAmount;
    private String billDueDate;
    private String transactionReference;

    /** DE-11 and DE-37 - kept so the caller can reconcile and query later. */
    private String stan;
    private String rrn;
    /** DE-38 authorisation id assigned by the switch. */
    private String authIdResponse;

    /** ISO 8583 DE-39 answered by the switch. */
    private String switchResponseCode;
    /** Outcome reported by PKG_MW.BILL_PAYMENT. */
    private String coreResponseCode;
    private String responseDescription;
}
