package com.dfs.switchsimulator.enums;

public enum DATEENUM {
    // @formatter:off
    JS_DELIMITER("|"),
    TIME_LOCAL_TRANSACTION_DATE_FORMAT("HHmmss"),
    DATE_LOCAL_TRANSACTION_DATE_FORMAT("MMdd"),
    TRANSACTION_DATE_FORMAT("MMddHHmmss");


    // @formatter:on
    DATEENUM(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

}
