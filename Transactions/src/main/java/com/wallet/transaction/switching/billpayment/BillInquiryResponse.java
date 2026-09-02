package com.wallet.transaction.switching.billpayment;

import lombok.Data;
import lombok.NoArgsConstructor;

/** Bill details returned to the DFS application before it confirms a payment. */
@Data
@NoArgsConstructor
public class BillInquiryResponse {

    private String utilityCompanyCode;
    private String utilityCompanyName;
    private String consumerNo;

    /** Amount due on the bill, as held in TBL_TRANSACTION_MOCK.BILL_AMOUNT. */
    private String billAmount;
    private String billDueDate;
    /** "Y" when the bill has already been settled. */
    private String billPaid;

    private String stan;
    private String rrn;
    /** ISO 8583 DE-39 as answered by the switch. */
    private String responseCode;
    private String responseDescription;
}
