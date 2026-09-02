package com.dfs.switchgateway.dto.TSDtos;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * What the transaction layer posts for a Bill Inquiry or a Bill Payment.
 *
 * One request type serves both messages: they carry the same data elements and differ only in
 * DE-03 and in whether an amount is present, so the gateway takes the processing code from the
 * route that was called rather than from a flag in the body.
 */
@Data
@NoArgsConstructor
public class BillRqst {

    /** DE-02 */
    private String pan;
    /** DE-04, major units; blank on an inquiry. */
    private String transactionAmount;
    /** DE-07 as ddMMyyyyHHmmss. */
    private String transactionDateTime;
    /** DE-18 */
    private String merchantType;
    /** DE-22 */
    private String pointOfEntry;
    /** DE-24 */
    private String networkIdentifier;
    /** DE-38, present on the payment. */
    private String authIdResp;
    /** DE-41 */
    private String cardAcceptorTerminalId;
    /** DE-42 */
    private String cardAcceptorIdentificationCode;
    /** DE-43, 40 characters. */
    private String cardAcceptorNameAndLocation;
    /** DE-49 */
    private String currencyCode;
    /** DE-102, the payer's own account. */
    private String accountNo1;
    /** DE-103, the consumer number being billed. */
    private String consumerNo;

    private String utilityCompanyCode;
    private String utilityCompanyName;
    private String billAmount;
    private String billDueDate;

    /** DE-11 */
    private String stan;
    /** DE-37 */
    private String rrn;
}
