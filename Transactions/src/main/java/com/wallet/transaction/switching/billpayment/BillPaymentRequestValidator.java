package com.wallet.transaction.switching.billpayment;

import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.util.ValidationException;

import java.math.BigDecimal;

/**
 * Input validation for the bill payment APIs, in the same style as
 * {@code com.wallet.transaction.util.RequestValidator} and {@code IbftRequestValidator}: a missing
 * or malformed field throws {@link ValidationException}, which the existing exception handling
 * turns into the standard error response.
 *
 * Only the presence and shape of what the application must send is checked here. Whether the bill
 * exists, whether it is already paid and whether the customer can afford it are decided by
 * TBL_TRANSACTION_MOCK and by the stored procedure.
 */
public final class BillPaymentRequestValidator {

    private BillPaymentRequestValidator() {
    }

    /** Bill Inquiry needs the payer (for DE-02) and the bill's identity. */
    public static void validateBillInquiry(BillPaymentRequest request, Request envelope) {
        requireEnvelope(envelope);
        requirePayerAndBill(request);
    }

    /** Bill Payment needs everything the inquiry needs, plus an amount to pay. */
    public static void validateBillPayment(BillPaymentRequest request, Request envelope) {
        requireEnvelope(envelope);
        requirePayerAndBill(request);

        // PKG_MW.BILL_PAYMENT resolves the payer from this, matched against TBL_CUSTOMER.NID_NO.
        // The inquiry never calls a procedure, so it is not required there.
        if (isBlank(request.getFromAccountNid())) {
            throw new ValidationException("From Account NID Required");
        }

        if (isBlank(request.getAmount())) {
            throw new ValidationException("Amount Required");
        }
        BigDecimal amount;
        try {
            amount = new BigDecimal(request.getAmount().trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("INVALID AMOUNT");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount Must Be Greater Than Zero");
        }
    }

    private static void requirePayerAndBill(BillPaymentRequest request) {
        // The message still identifies the payer to the switch: DE-02 is built from this account.
        if (isBlank(request.getFromAccountNo())) {
            throw new ValidationException("From Account No Required");
        }
        if (isBlank(request.getUtilityCompanyCode())) {
            throw new ValidationException("Utility Company Code Required");
        }
        if (isBlank(request.getConsumerNo())) {
            throw new ValidationException("Consumer No Required");
        }
    }

    private static void requireEnvelope(Request envelope) {
        if (envelope == null) {
            throw new ValidationException("INVALID REQUEST");
        }
        if (isBlank(envelope.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (isBlank(envelope.getChannel())) {
            throw new ValidationException("INVALID CHANNEL");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
