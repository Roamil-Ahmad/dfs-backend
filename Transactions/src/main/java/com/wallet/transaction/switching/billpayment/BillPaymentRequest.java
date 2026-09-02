package com.wallet.transaction.switching.billpayment;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload the DFS application sends for a bill inquiry or a bill payment.
 *
 * Business information only, and it follows the Pakistan UBP shape the app already uses elsewhere:
 * a utility company identified by its code from TBL_UBP_COMPANY, plus the consumer number printed
 * on the bill.
 *
 * Technical 1LINK data elements - DE-02, STAN, RRN, acquirer IMD, merchant type, point of entry,
 * card acceptor details - are not exposed to the application: they are generated or configured
 * inside the transaction layer. The biller's name, the amount due and the due date are not accepted
 * from the caller either; they are resolved from TBL_TRANSACTION_MOCK so they cannot be spoofed.
 */
@Data
@NoArgsConstructor
public class BillPaymentRequest {

    /**
     * Customer account the funds leave from. For a DFS wallet this is the customer's 11-digit
     * mobile number, which is also the account number in TBL_ACCOUNT.
     *
     * <p>Required on both endpoints: DE-02 is built from it, as the DFS BIN followed by the mobile
     * number without its leading zero.</p>
     */
    private String fromAccountNo;

    /**
     * National ID of the paying customer - TBL_CUSTOMER.NID_NO.
     *
     * <p>Required because PKG_MW.BILL_PAYMENT resolves the payer from it, the same way
     * OUTGOING_IBFT does: with no "=" the value is treated as a national ID and matched against
     * TBL_CUSTOMER.NID_NO. Sent as the plain number - the service encrypts it to match the
     * stored ciphertext.</p>
     */
    private String fromAccountNid;

    /** Utility company code, as listed by {@code /v1/getbiller} from TBL_UBP_COMPANY.CODE. */
    private String utilityCompanyCode;

    /** Consumer number printed on the bill. */
    private String consumerNo;

    /**
     * Amount to pay, in major units, e.g. "1500.00". Not required for an inquiry; the inquiry
     * answers with the amount actually due.
     */
    private String amount;

    /** Optional client-side reference echoed back on the response. */
    private String transactionReference;
}
