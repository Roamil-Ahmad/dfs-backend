package com.wallet.transaction.dto;

public class AgentCashInResponse {
    private Long transHeadId;
    private String responseCode;
    private Integer responseStatus;
    private String responseDescription;

    // Getters and setters
    public Long getTransHeadId() {
        return transHeadId;
    }

    public void setTransHeadId(Long transHeadId) {
        this.transHeadId = transHeadId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }
}