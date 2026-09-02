package com.wallet.transaction.switching.common.client;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * What the transaction layer sends to the switch gateway for a bill inquiry or a bill payment.
 *
 * One request type serves both: the two 1LINK messages carry the same data elements and differ
 * only in DE-03 and in whether an amount is present, so the gateway picks the processing code from
 * the endpoint that was called rather than from a flag in here.
 */
@Data
@NoArgsConstructor
public class GatewayBillRequest {

    /** DE-02, the 16-digit PAN derived from the payer's mobile number. */
    private String pan;
    /** DE-04, major units; blank or zero on an inquiry. */
    private String transactionAmount;
    /** DE-07 as ddMMyyyyHHmmss. */
    private String transactionDateTime;
    /** DE-18 */
    private String merchantType;
    /** DE-22 */
    private String pointOfEntry;
    /** DE-24 */
    private String networkIdentifier;
    /** DE-41 */
    private String cardAcceptorTerminalId;
    /** DE-42 */
    private String cardAcceptorIdentificationCode;
    /** DE-43, 40 characters */
    private String cardAcceptorNameAndLocation;
    /** DE-49 */
    private String currencyCode;
    /** DE-102, the payer's own account. */
    private String accountNo1;
    /** DE-103, the consumer number being billed. */
    private String consumerNo;

    /** Utility company code, from TBL_UBP_COMPANY.CODE. */
    private String utilityCompanyCode;
    /** Utility company name, resolved from TBL_TRANSACTION_MOCK - never sent by the app. */
    private String utilityCompanyName;
    /** Amount due, resolved from TBL_TRANSACTION_MOCK. */
    private String billAmount;
    /** Due date, resolved from TBL_TRANSACTION_MOCK. */
    private String billDueDate;

    /** DE-11 */
    private String stan;
    /** DE-37 */
    private String rrn;
    /** DE-38, present on the payment once the core has authorised the debit. */
    private String authIdResp;
}
