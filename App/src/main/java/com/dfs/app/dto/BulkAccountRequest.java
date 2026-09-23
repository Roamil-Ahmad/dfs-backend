package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Payload for the bulk account upload.
 *
 * <p>A list because the table is a bulk intake: one account is a list of one, and the portal can
 * send a whole batch in a single call rather than one request per row.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BulkAccountRequest {
    private List<BulkAccount> accounts;
}
