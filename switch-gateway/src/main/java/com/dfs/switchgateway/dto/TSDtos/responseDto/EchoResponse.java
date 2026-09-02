package com.dfs.switchgateway.dto.TSDtos.responseDto;

/*
Author Name: abdul.fatah

Project Name: switch-gateway

Package Name: com.dfs.switchgateway.dto.TSDtos.responseDto

Class Name: EchoResponse

Date and Time:7/17/2023 12:10 PM

Version:1.0
*/
public class EchoResponse {
    private String mti;
    private String transactionDate;
    private String stan;
    private String networkManagementCode;
    private String networkIdentifier;
    private String responseCode;

    public String getMti() {
        return mti;
    }

    public void setMti( String mti ) {
        this.mti = mti;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getStan() {
        return stan;
    }

    public void setStan( String stan ) {
        this.stan = stan;
    }

    public String getNetworkManagementCode() {
        return networkManagementCode;
    }

    public void setNetworkManagementCode( String networkManagementCode ) {
        this.networkManagementCode = networkManagementCode;
    }

    public String getNetworkIdentifier() {
        return networkIdentifier;
    }

    public void setNetworkIdentifier( String networkIdentifier ) {
        this.networkIdentifier = networkIdentifier;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode( String responseCode ) {
        this.responseCode = responseCode;
    }
}
