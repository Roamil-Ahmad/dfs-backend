package com.wallet.transaction.switching.common;

/**
 * Fixed values shared by the switched transaction flows.
 *
 * Only protocol-level constants live here. Anything institution-specific (IMD, terminal id,
 * channel) comes from configuration, and anything transaction-specific comes from the request,
 * the mock table or the database procedure.
 */
public final class SwitchingConstants {

    private SwitchingConstants() {
    }

    /** DE-39 "PROCESSED OK" - 1LINK spec section 9.30. */
    public static final String RC_PROCESSED_OK = "00";
    /** DE-39 "INVALID TO ACCOUNT". */
    public static final String RC_INVALID_TO_ACCOUNT = "68";
    /** DE-39 "UNABLE TO PROCESS". */
    public static final String RC_UNABLE_TO_PROCESS = "46";
    /** DE-39 "INTERNAL DATABASE ERROR". */
    public static final String RC_INTERNAL_DATABASE_ERROR = "13";
    /** DE-39 "HOST LINK DOWN". */
    public static final String RC_HOST_LINK_DOWN = "55";
    /** DE-39 "DUPLICATE TRANSACTION" - used when a bill is already settled. */
    public static final String RC_DUPLICATE_TRANSACTION = "94";

    /** DE-03 processing codes - 1LINK spec section 9.4. */
    public static final String PROCESSING_CODE_TITLE_FETCH = "620000";
    public static final String PROCESSING_CODE_IBFT = "480000";

    /** DE-120 identifier - 1LINK spec 9.62.1.1 sub-field 5. */
    public static final String IDENTIFIER_DEBIT = "D";
    public static final String IDENTIFIER_CREDIT = "C";
}
