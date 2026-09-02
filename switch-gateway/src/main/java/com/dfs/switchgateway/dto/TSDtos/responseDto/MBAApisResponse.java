package com.dfs.switchgateway.dto.TSDtos.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
Author Name: abdul.fatah

Project Name: switch-gateway

Package Name: com.dfs.switchgateway.dto.TSDtos.responseDto

Class Name: MBAApisResponse

Date and Time:7/18/2023 1:03 PM

Version:1.0
*/
public class MBAApisResponse {
    @JsonProperty("responseCode")
    private String ResponseCode;
    @JsonProperty("responseDescription")
    private String ResponseDescription;
    @JsonProperty("rrn")
    private String Rrn;
    @JsonProperty("cnic")
    private String cnic;
    @JsonProperty("customerName")
    private String CustomerName;
    @JsonProperty("accountTitle")
    private String AccountTitle;
    @JsonProperty("mobileNumber")
    private String MobileNumber;
    @JsonProperty("dateTime")
    private String DateTime;
    @JsonProperty("balance")
    private String Balance;
    @JsonProperty("remainingDebitLimit")
    private String RemainingDebitLimit;
    @JsonProperty("remainingCreditLimit")
    private String RemainingCreditLimit;
    @JsonProperty("consumedVelocity")
    private String ConsumedVelocity;
    @JsonProperty("dateHash")
    private String DateHash;
    @JsonProperty("stan")
    private String stan;
    private String responseDateTime;

    public String getResponseDateTime() {
        return responseDateTime;
    }

    public void setResponseDateTime(String responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getResponseDescription() {
        return ResponseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        ResponseDescription = responseDescription;
    }

    public String getRrn() {
        return Rrn;
    }

    public void setRrn(String rrn) {
        Rrn = rrn;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getAccountTitle() {
        return AccountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        AccountTitle = accountTitle;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getRemainingDebitLimit() {
        return RemainingDebitLimit;
    }

    public void setRemainingDebitLimit(String remainingDebitLimit) {
        RemainingDebitLimit = remainingDebitLimit;
    }

    public String getRemainingCreditLimit() {
        return RemainingCreditLimit;
    }

    public void setRemainingCreditLimit(String remainingCreditLimit) {
        RemainingCreditLimit = remainingCreditLimit;
    }

    public String getConsumedVelocity() {
        return ConsumedVelocity;
    }

    public void setConsumedVelocity(String consumedVelocity) {
        ConsumedVelocity = consumedVelocity;
    }

    public String getDateHash() {
        return DateHash;
    }

    public void setDateHash(String dateHash) {
        DateHash = dateHash;
    }
}
