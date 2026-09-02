package com.barq.nadra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RESPONSESTATUS {
    @JsonProperty("CODE")
    private String code;
    @JsonProperty("MESSAGE")
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
