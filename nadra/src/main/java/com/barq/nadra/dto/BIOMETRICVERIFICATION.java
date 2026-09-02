package com.barq.nadra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BIOMETRICVERIFICATION {
    @JsonProperty("RESPONSE_DATA")
    private RESPONSEDATA responsedata;

    public RESPONSEDATA getResponsedata() {
        return responsedata;
    }

    public void setResponsedata(RESPONSEDATA responsedata) {
        this.responsedata = responsedata;
    }
}
