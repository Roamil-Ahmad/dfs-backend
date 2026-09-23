package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for the bulk account upload: one account, one row.
 *
 * <p>The segment is not here. It is taken from {@code segment} on the request envelope, the field
 * the platform already carries for it, rather than repeated inside the payload.</p>
 *
 * <p>The audit columns of TBL_BULK_ACCOUNTS are set by the service, not by the request.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BulkAccountRequest {
    private String mobileNo;
    private String accountTitle;
    private String nidNo;
}
