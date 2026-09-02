package com.dfs.switchgateway.dto.TSDtos.responseDto;

import lombok.Data;
import lombok.NoArgsConstructor;

/** What the gateway hands back to the transaction layer for a bill inquiry or payment. */
@Data
@NoArgsConstructor
public class BillResponse {

    private String pan;
    private String stan;
    private String rrn;
    /** DE-39 */
    private String responseCode;
    /** DE-38 */
    private String authIdResp;
    private String transactionAmount;

    private String utilityCompanyCode;
    private String utilityCompanyName;
    private String consumerNo;
    private String billAmount;
    private String billDueDate;
    /** DE-120 sub-field: whether the biller reports the bill as settled. */
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
