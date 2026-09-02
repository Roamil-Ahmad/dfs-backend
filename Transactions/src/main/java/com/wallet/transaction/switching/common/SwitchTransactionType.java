package com.wallet.transaction.switching.common;

/**
 * Transaction types routed through the payment switch.
 *
 * The value is what {@code TBL_TRANSACTION_MOCK.TRANSACTION_TYPE} holds, so a lookup for a flow is
 * always driven by this enum rather than by a literal scattered through the services.
 *
 * Only {@link #IBFT} is implemented today; the other two are declared so the mock lookup and the
 * package layout already carry them when those flows are built.
 */
public enum SwitchTransactionType {

    IBFT("IBFT"),
    BILL_PAYMENT("BILL_PAYMENT"),
    WALLET_TO_WALLET("WALLET_TO_WALLET");

    private final String value;

    SwitchTransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
