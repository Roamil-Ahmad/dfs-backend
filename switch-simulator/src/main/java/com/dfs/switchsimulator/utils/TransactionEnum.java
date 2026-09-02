package com.dfs.switchsimulator.utils;

public enum TransactionEnum {
    JS_DELIMITER("|"),
    TIME_LOCAL_TRANSACTION_DATE_FORMAT("HHmmss"),
    DATE_LOCAL_TRANSACTION_DATE_FORMAT("MMdd"),
    TRANSACTION_DATE_FORMAT("MMddHHmmss");

    TransactionEnum( String value ) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

}
