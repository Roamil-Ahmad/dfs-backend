package com.wallet.transaction.dto;

public class WalletToWalletResponse {
    private String authIdResponse;
    private String responseCode;
    private String availableBalance;
    private String actualBalance;
    private String responseDescription;
    private Integer responseStatus;
    private String checkPoint;
    private Long transHeadId;

    // Getters and setters
    public String getAuthIdResponse() {
        return authIdResponse;
    }

    public void setAuthIdResponse(String authIdResponse) {
        this.authIdResponse = authIdResponse;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getActualBalance() {
        return actualBalance;
    }

    public void setActualBalance(String actualBalance) {
        this.actualBalance = actualBalance;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(String checkPoint) {
        this.checkPoint = checkPoint;
    }

    public Long getTransHeadId() {
        return transHeadId;
    }

    public void setTransHeadId(Long transHeadId) {
        this.transHeadId = transHeadId;
    }
}