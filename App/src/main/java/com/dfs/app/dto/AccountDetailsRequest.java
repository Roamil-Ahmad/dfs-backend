package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload for the corporate account lookup: the wallet to describe.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountDetailsRequest {
    private String mobileNumber;
}
