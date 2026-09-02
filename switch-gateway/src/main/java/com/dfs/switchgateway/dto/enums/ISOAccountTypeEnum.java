package com.dfs.switchgateway.dto.enums;

public enum ISOAccountTypeEnum {
    // @formatter:off
    DEFAULT("00"),
    SAVING("10"),
    CHECKING("20"),
    DEBIT_CARD("25"),
    CREDIT_CARD("30"),
    BALANCE_TRANSFER_FACILITY("45"),
    FIXED_DEPOSIT_ACCOUNT("50");

    // @formatter:on
    ISOAccountTypeEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

}
