package com.wallet.transaction.switching.ibft;

import com.wallet.transaction.dto.common.Request;
import com.wallet.transaction.switching.ibft.outgoing.OutgoingIbftRequest;

import com.wallet.transaction.util.ValidationException;
import java.math.BigDecimal;

/**
 * Input validation for the IBFT APIs.
 *
 * Mirrors the style of {@code com.wallet.transaction.util.RequestValidator}: a missing or malformed
 * field throws {@link com.wallet.transaction.util.ValidationException}, which the existing exception handling turns into the
 * standard error response.
 *
 * Only the presence and shape of what the application must send is checked here. Whether the
 * beneficiary exists, whether the balance is sufficient and whether the transfer may proceed are
 * decided by TBL_TRANSACTION_MOCK and by PKG_MW, not by this class.
 */
public final class IbftRequestValidator {

    private IbftRequestValidator() {
    }

    public static void validateOutgoingTitleFetch(OutgoingIbftRequest request, Request envelope) {
        requireEnvelope(envelope);
        // The inquiry still identifies the sender to the switch: DE-02 is built from this account.
        if (isBlank(request.getFromAccountNo())) {
            throw new ValidationException("From Account No Required");
        }
        // The customer picks the destination bank on screen. It is mandatory because an account
        // number is only unique within a bank -- DFS must never guess where the money goes.
        if (isBlank(request.getBeneficiaryBankImd())) {
            throw new ValidationException("Beneficiary Bank Required");
        }
        if (isBlank(request.getBeneficiaryAccountNo())) {
            throw new ValidationException("Beneficiary Account No Required");
        }
    }

    public static void validateOutgoingTransfer(OutgoingIbftRequest request, Request envelope) {
        requireEnvelope(envelope);
        if (isBlank(request.getFromAccountNo())) {
            throw new ValidationException("From Account No Required");
        }
        // The customer picks the destination bank on screen. It is mandatory because an account
        // number is only unique within a bank -- DFS must never guess where the money goes.
        if (isBlank(request.getBeneficiaryBankImd())) {
            throw new ValidationException("Beneficiary Bank Required");
        }
        if (isBlank(request.getBeneficiaryAccountNo())) {
            throw new ValidationException("Beneficiary Account No Required");
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
        if (isBlank(request.getPurposeOfPayment())) {
            throw new ValidationException("Purpose Of Payment Required");
        }
        // Only the transfer needs it: it is what PKG_MW matches on TBL_CUSTOMER.NID_NO to find
        // the paying customer. The title fetch never calls a procedure, so it does not apply there.
        if (isBlank(request.getFromAccountNid())) {
            throw new ValidationException("From Account NID Required");
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
