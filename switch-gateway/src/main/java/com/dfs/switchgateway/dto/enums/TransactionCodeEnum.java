/*******************************************************************************
 * Copyrights Inov8
 ******************************************************************************/
package com.dfs.switchgateway.dto.enums;

/**
 * An enum which contains possible transaction set that our application will send to
 * external system.
 */
public enum TransactionCodeEnum {

    JS_ACCOUNT_BALANCE_INQUIRY("310000"),
    JS_BILL_INQUIRY("720000"),
    JS_BILL_PAYMENT("990000"),
    JS_TITLE_FETCH("930000"),
    JS_FUND_TRANSFER_ADVICE("950000"),
    JS_ACQUIRER_REVERSAL_ADVICE("950000"),
    JS_FUND_TRANSFER("950000"),

    IBFT_TITLE_FETCH("620000"),
    IBFT_ADVICE("480000"),

    JS_BB_TITLE_FETCH("621000"), // HOST TRANSACTION
    JS_BB_ACCOUNT_FT_ADVICE("910000"), // HOST TRANSACTION
    JS_BB_IBFT_WALLET_CREDIT_ADVICE("970000"); // HOST TRANSACTION

    // @formatter:on
    TransactionCodeEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
