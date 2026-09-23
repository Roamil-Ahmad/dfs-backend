package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * One account in a bulk submission.
 *
 * <p>Mirrors the columns of TBL_BULK_ACCOUNTS that the caller supplies; the audit columns are set
 * by the service, not by the request.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BulkAccount {
    private String mobileNo;
    private String accountTitle;
    private String nidNo;
    private String segmentDescr;
}
