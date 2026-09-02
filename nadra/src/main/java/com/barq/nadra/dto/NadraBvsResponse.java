package com.barq.nadra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NadraBvsResponse {
    private String processingCode;
    private String merchantType;
    private String traceNo;
    private String companyName;
    private String dateTime;
    @JsonProperty("VerifyFingerPrintsResponse")
    private VerifyFingerPrintsResponse verifyFingerPrintsResponse;

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public VerifyFingerPrintsResponse getVerifyFingerPrintsResponse() {
        return verifyFingerPrintsResponse;
    }

    public void setVerifyFingerPrintsResponse(VerifyFingerPrintsResponse verifyFingerPrintsResponse) {
        this.verifyFingerPrintsResponse = verifyFingerPrintsResponse;
    }
}
