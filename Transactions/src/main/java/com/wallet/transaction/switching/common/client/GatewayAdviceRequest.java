package com.wallet.transaction.switching.common.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * IBFT Advice request as the switch gateway expects it.
 *
 * Field names follow the "1link Apis Document" IBFT Advice request.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatewayAdviceRequest {

    private String pan;
    private String transactionAmount;
    private String merchantType;
    private String pointOfEntry;
    /** DE-38, echoed from the title fetch */
    private String authIdResp;
    private String cardAcceptorTerminalId;
    private String cardAcceptorIdentificationCode;
    private String cardAcceptorNameAndLocation;
    private String purposeOfPayment;
    private String currencyCode;
    /** DE-102 - sender account */
    private String accountNo1;
    /** DE-103 - beneficiary account */
    private String accountNo2;
    /** DE-120 sub-field 2 */
    private String accountTitle;
    /** DE-120 sub-field 8 */
    private String senderName;
    /** DE-120 sub-field 4 */
    private String toBankImd;
    private String stan;
    private String rrn;
    /** DE-120 sub-field 14 */
    private String beneficiaryId;
    private String transactionDateTime;
    /** DE-120 sub-field 6 */
    private String accountBankName;
    /** DE-120 sub-field 7 */
    private String accountBranchName;
    /** DE-120 sub-field 13 */
    private String senderId;
    private String networkIdentifier;
}
