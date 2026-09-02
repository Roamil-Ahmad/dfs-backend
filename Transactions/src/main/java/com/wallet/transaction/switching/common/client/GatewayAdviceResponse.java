package com.wallet.transaction.switching.common.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/** IBFT Advice response returned by the switch gateway. */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatewayAdviceResponse {

    private String stan;
    private String rrn;
    /** ISO 8583 DE-39 */
    private String responseCode;
    private String transDateTime;
    private String transmissionTime;
    private String transactionAmount;
    private String accountTitle;
    private String accountNo1;
    private String accountNo2;
    private String accountBankName;
    private String accountBranchName;
    private String toBankImd;
    private String senderName;
    private String senderId;
    private String receiverId;
    private String authIdResp;
    private String identifier;
    private String pan;
    private String purposeOfPayment;
    private String message;
}
