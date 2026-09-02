package com.wallet.transaction.switching.common.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/** Bill inquiry / bill payment response returned by the switch gateway. */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GatewayBillResponse {

    private String pan;
    private String stan;
    private String rrn;
    /** ISO 8583 DE-39 */
    private String responseCode;
    /** DE-38 */
    private String authIdResp;
    private String transactionAmount;

    private String utilityCompanyCode;
    private String utilityCompanyName;
    private String consumerNo;
    private String billAmount;
    private String billDueDate;
    private String billStatus;

    private String accountNo1;
    private String merchantType;
    private String networkIdentifier;
    private String pointOfEntry;
    private String cardAcceptorNameAndLocation;
    private String cardAcceptorTerminalId;
    private String dateLocalTransaction;
    private String timeLocalTransaction;
    private String message;
}
