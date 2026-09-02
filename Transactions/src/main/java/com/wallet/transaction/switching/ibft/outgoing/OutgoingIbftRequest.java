package com.wallet.transaction.switching.ibft.outgoing;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload the DFS application sends for an outgoing IBFT.
 *
 * Business information only. Technical 1LINK data elements - STAN, RRN, acquirer IMD, merchant
 * type, point of entry, card acceptor details - are not exposed to the application: they are
 * generated or configured inside the transaction layer. Beneficiary title, bank name and
 * destination IMD are not accepted from the caller either; they are resolved from
 * TBL_TRANSACTION_MOCK so they cannot be spoofed.
 */
@Data
@NoArgsConstructor
public class OutgoingIbftRequest {

    /**
     * Customer account the funds leave from. For a DFS wallet this is the customer's 11-digit
     * mobile number, which is also the account number in TBL_ACCOUNT.
     *
     * <p>It is required on both endpoints: DE-02 is built from it, as the DFS BIN followed by the
     * mobile number without its leading zero.</p>
     */
    private String fromAccountNo;

    /**
     * National ID of the customer the funds leave from - TBL_CUSTOMER.NID_NO.
     *
     * <p>This is what PKG_MW calls the relationship id. OUTGOING_IBFT accepts it as either
     * "PAN=EXPIRY" or a bare national ID; with no "=" it takes the CNIC branch, which resolves
     * the customer with</p>
     *
     * <pre>SELECT CUSTOMER_ID FROM TBL_CUSTOMER WHERE NID_NO = TRIM(P_RELATIONSHIPID)</pre>
     *
     * <p>and then requires that customer to own {@link #fromAccountNo}. So the account number
     * alone cannot stand in for it: sending the account number made that lookup miss and the
     * debit was refused before it ever reached the switch.</p>
     *
     * <p>Sent verbatim. No reformatting here - the procedure matches the stored value exactly,
     * so any normalising has to happen where the value is captured, not in transit.</p>
     */
    private String fromAccountNid;

    /** Beneficiary account or IBAN at the receiving bank. */
    private String beneficiaryAccountNo;
    /**
     * The bank the customer picked, as its 1LINK IMD (LKP_BANK.BANK_IMD, which in this schema
     * equals BANK_CODE). Mandatory: an account number is only unique within a bank, so the
     * destination institution has to come from the customer's selection, never be guessed.
     */
    private String beneficiaryBankImd;

    /** Transfer amount in major units, e.g. "1500.00". */
    private String amount;

    /**
     * 1LINK product category code, spec Appendix-A. For example 0401 Home Remittance,
     * 0376 Credit Card Bill Payment, 9999 free text.
     */
    private String purposeOfPayment;

    /** Optional client-side reference echoed back on the response. */
    private String transactionReference;
}
