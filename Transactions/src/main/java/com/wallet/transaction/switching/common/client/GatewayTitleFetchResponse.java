package com.wallet.transaction.switching.common.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/** Account Title Inquiry response returned by the switch gateway. */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatewayTitleFetchResponse {

    private String pan;
    private String stan;
    private String rrn;
    /** ISO 8583 DE-39 */
    private String responseCode;
    private String transactionAmount;
    /** DE-120 sub-field 2 */
    private String accountTitle;
    private String accountNo1;
    private String accountNo2;
    /** DE-120 sub-field 4 */
    private String toBankImd;
    /** DE-120 sub-field 5 */
    private String identifier;
    /** DE-120 sub-field 14 */
    private String beneficiaryId;
    private String accountBankName;
    private String accountBranchName;
    private String merchantType;
    private String networkIdentifier;
    private String pointOfEntry;
    private String purposeOfPayment;
    private String cardAcceptorNameAndLocation;
    private String cardAcceptorTerminalId;
    private String dateLocalTransaction;
    private String timeLocalTransaction;
    private String message;
}
