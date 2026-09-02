package com.barq.nadra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
@SuppressWarnings({"java:S117","java:S100","java:S101","java:S116"})
public class RESPONSE_STATUS {
    @JsonProperty("MESSAGE")
    public String MESSAGE;
    @JsonProperty("CODE")
    public int CODE;

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public int getCODE() {
        return CODE;
    }

    public void setCODE(int CODE) {
        this.CODE = CODE;
    }
}
