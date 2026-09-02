package com.barq.nadra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings({"java:S101","java:S116","java:S100","java:S117"})
public class BIOMETRIC_VERIFICATION {
    @JsonProperty("xmlns:xsd")
    public String xmlns;
    @JsonProperty("RESPONSE_DATA")
    public RESPONSE_DATA RESPONSE_DATA;
    @JsonProperty("xmlns:xsi")
    public String xmlni;

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public com.barq.nadra.dto.RESPONSE_DATA getRESPONSE_DATA() {
        return RESPONSE_DATA;
    }

    public void setRESPONSE_DATA(com.barq.nadra.dto.RESPONSE_DATA RESPONSE_DATA) {
        this.RESPONSE_DATA = RESPONSE_DATA;
    }

    public String getXmlni() {
        return xmlni;
    }

    public void setXmlni(String xmlni) {
        this.xmlni = xmlni;
    }
}
