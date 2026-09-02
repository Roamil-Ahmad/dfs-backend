package com.dfs.switchsimulator.enums;

public enum ProcessingCodeEnum {


    TITLE_FETCH(620000),
    IBFT_ADVICE(480000);

    int value;

    ProcessingCodeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }



}
