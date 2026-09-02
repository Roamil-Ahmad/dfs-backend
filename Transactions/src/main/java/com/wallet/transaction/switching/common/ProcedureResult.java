package com.wallet.transaction.switching.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Outcome of a PKG_MW stored procedure call.
 *
 * The procedure owns the accounting; this object only carries what it reported back so the service
 * layer can map it onto an API response.
 */
@Data
@NoArgsConstructor
public class ProcedureResult {

    /** P_AUTHIDRESPONSE - the authorisation id the core assigned (DE-38). */
    private String authIdResponse;
    /** P_ERRORRESPONSE - the core's error/response code. */
    private String errorResponse;
    /** P_RESPONSEDESCR - human readable description of the outcome. */
    private String responseDescription;
    /**
     * P_RESPONSESTATUS - 1 means the procedure ran to completion, 0 means it aborted.
     * Confirmed against the package body: every validator opens with P_RESPONSESTATUS := 1 and
     * drops it to 0 on a validation failure or in WHEN OTHERS, and the callers inside PKG_MW
     * gate on "IF P_RESPONSESTATUS = 0 THEN ... RETURN".
     */
    private Integer responseStatus;
    /** P_CHECKPOINT - where the procedure got to; invaluable while the package body is broken. */
    private String checkpoint;
    /** P_TRANS_HEAD_ID - identity of the posted transaction. */
    private Long transHeadId;
    /** P_RECORDDATA - IN/OUT, the procedure may rewrite DE-120. */
    private String recordData;
    /** P_UDF1 - IN/OUT user defined field. */
    private String udf1;
    /** P_AVAILABLEBALANCE - returned by PKG_MW.BILL_PAYMENT; null for procedures that omit it. */
    private String availableBalance;
    /** P_ACTUALBALANCE - returned by PKG_MW.BILL_PAYMENT; null for procedures that omit it. */
    private String actualBalance;

    /** Approval codes: PKG_MW returns 000, PKG_PAYMENTS1 returns 000 or 0000. */
    private static final java.util.Set<String> APPROVED_CODES = java.util.Set.of("000", "0000");

    /**
     * True when the procedure ran to completion rather than aborting.
     *
     * <p>This is not the same as the money having moved. A business decline still completes:
     * PKG_MW low balance sets P_ERRORRESPONSE 004 alongside P_RESPONSESTATUS 1. Use
     * {@link #isApproved()} for the "was the transaction actually posted" decision.</p>
     */
    public boolean isSuccessful() {
        return responseStatus != null && responseStatus == 1;
    }

    /**
     * True only when the core approved and posted the transaction: the procedure completed AND
     * returned its approval code. Anything else - a decline, a low balance, an internal error -
     * is not an approval and must never be reported as one, nor advised to the switch.
     */
    public boolean isApproved() {
        return isSuccessful()
                && errorResponse != null
                && APPROVED_CODES.contains(errorResponse.trim());
    }

    /**
     * The code to report when the call did not end in an approval: the core's own
     * P_ERRORRESPONSE, passed through untranslated so the caller sees exactly what the procedure
     * decided. Only when the procedure gave no code at all - it died before setting one - does
     * the fallback apply.
     */
    public String failureCode(String fallback) {
        return errorResponse == null || errorResponse.trim().isEmpty() ? fallback : errorResponse.trim();
    }

    /**
     * The core's own P_RESPONSEDESCR, verbatim. It carries the detail nothing else has - the
     * failing validator name and any ORA- error - so it is never rewritten into a generic message.
     */
    public String failureDescription(String fallback) {
        return responseDescription == null || responseDescription.trim().isEmpty()
                ? fallback : responseDescription.trim();
    }
}
