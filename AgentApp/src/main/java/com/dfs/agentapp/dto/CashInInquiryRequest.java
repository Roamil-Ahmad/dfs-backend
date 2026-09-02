package com.dfs.agentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Looks the depositor up before an agent cash-in.
 *
 * Both values are required: the account number alone would let an agent read back any account
 * holder's name, so the CNIC has to match the account before anything is returned.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CashInInquiryRequest {

    /** The wallet account being credited; for a DFS wallet this is the customer's mobile number. */
    private String accountNo;

    /** CNIC of the person handing the cash over, which must be the account holder's. */
    private String depositorNid;
}
