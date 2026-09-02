package com.dfs.app.util;

public enum FieldValidation {
    NUmBER_VALIDATOR("//d+");
    private String value;

    FieldValidation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
