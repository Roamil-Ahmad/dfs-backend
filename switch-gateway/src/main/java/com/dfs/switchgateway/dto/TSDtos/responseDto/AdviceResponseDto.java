package com.dfs.switchgateway.dto.TSDtos.responseDto;

/*
Author Name: abdul.fatah

Project Name: switch-gateway

Package Name: com.dfs.switchgateway.dto.TSDtos.responseDto

Class Name: AdviceResponseDto

Date and Time:8/25/2023 12:04 PM

Version:1.0
*/
public class AdviceResponseDto {
    private String rrn;
    private String responseCode;
    private String responseDescription;
    private String responseDateTime;
    private String transactionId;
    private String commissionAmount;
    private String transactionAmount;
    private String totalTransactionAmount;
    private String hashData;

    private String stan;

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn( String rrn ) {
        this.rrn = rrn;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode( String responseCode ) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription( String responseDescription ) {
        this.responseDescription = responseDescription;
    }

    public String getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime( String responseDateTime ) {
        this.responseDateTime = responseDateTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId( String transactionId ) {
        this.transactionId = transactionId;
    }

    public String getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount( String commissionAmount ) {
        this.commissionAmount = commissionAmount;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount( String transactionAmount ) {
        this.transactionAmount = transactionAmount;
    }

    public String getTotalTransactionAmount() {
        return totalTransactionAmount;
    }

    public void setTotalTransactionAmount( String totalTransactionAmount ) {
        this.totalTransactionAmount = totalTransactionAmount;
    }

    public String getHashData() {
        return hashData;
    }

    public void setHashData( String hashData ) {
        this.hashData = hashData;
    }
}
