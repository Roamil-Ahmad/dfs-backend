package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * What the corporate account lookup returns.
 *
 * <p>{@code nidNo} and {@code accountTitle} are decrypted before they leave: both are held
 * encrypted at rest, and the portal has no key of its own.</p>
 *
 * <p>{@code nidNo}, {@code gender} and {@code segmentDescr} come from the customer behind the
 * account. An agent account has no customer, so they are null there rather than an error.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountDetailsResponse {
    private String accountNo;
    private String mobileNo;
    private String nidNo;
    private String gender;
    private String segmentDescr;
    private String iban;
    private String qrCode;
    private String accountTitle;
    private BigDecimal currentBalance;
    private String accountStatusDescr;
}
