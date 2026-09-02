package com.dfs.thirdparties.util;

public enum FieldValidation {
    MOBILE_NUmBER_VALIDATOR("//d+");
    private String value;

    FieldValidation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
