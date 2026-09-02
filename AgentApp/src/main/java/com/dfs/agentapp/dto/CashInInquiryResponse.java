package com.dfs.agentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * What the agent app needs to confirm the depositor and to fill the cash-in request:
 * the name and date of birth go on to /v1/internalcashin as depositorName and depositorDob.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CashInInquiryResponse {

    private String accountNo;
    private String name;
    /** yyyy-MM-dd, the format /v1/internalcashin expects back. */
    private String dob;
}
