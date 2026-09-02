package com.wallet.transaction.dto.aps;



public class BalanceInquiryResp {

    private String stan;

    private String rrn;

    private String timeLocalTransaction;

    private String additionalAmounts;

    private String currencyCodeTransaction;

    private String additionalDataPrivate;

    private String responseCode;

    private String authorizationIdentificationResponse;

    private String cvc2Present;
    private String networkReferenceNumber;
    private String originalNetworkReference;
    private String availableBalance;
    private String currencyAvailable;


    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getTimeLocalTransaction() {
        return timeLocalTransaction;
    }

    public void setTimeLocalTransaction(String timeLocalTransaction) {
        this.timeLocalTransaction = timeLocalTransaction;
    }

    public String getAdditionalAmounts() {
        return additionalAmounts;
    }

    public void setAdditionalAmounts(String additionalAmounts) {
        this.additionalAmounts = additionalAmounts;
    }

    public String getCurrencyCodeTransaction() {
        return currencyCodeTransaction;
    }

    public void setCurrencyCodeTransaction(String currencyCodeTransaction) {
        this.currencyCodeTransaction = currencyCodeTransaction;
    }

    public String getAdditionalDataPrivate() {
        return additionalDataPrivate;
    }

    public void setAdditionalDataPrivate(String additionalDataPrivate) {
        this.additionalDataPrivate = additionalDataPrivate;
    }


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getAuthorizationIdentificationResponse() {
        return authorizationIdentificationResponse;
    }

    public void setAuthorizationIdentificationResponse(String authorizationIdentificationResponse) {
        this.authorizationIdentificationResponse = authorizationIdentificationResponse;
    }



    public String getCvc2Present() {
        return cvc2Present;
    }

    public void setCvc2Present(String cvc2Present) {
        this.cvc2Present = cvc2Present;
    }

    public String getNetworkReferenceNumber() {
        return networkReferenceNumber;
    }

    public void setNetworkReferenceNumber(String networkReferenceNumber) {
        this.networkReferenceNumber = networkReferenceNumber;
    }

    public String getOriginalNetworkReference() {
        return originalNetworkReference;
    }

    public void setOriginalNetworkReference(String originalNetworkReference) {
        this.originalNetworkReference = originalNetworkReference;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getCurrencyAvailable() {
        return currencyAvailable;
    }

    public void setCurrencyAvailable(String currencyAvailable) {
        this.currencyAvailable = currencyAvailable;
    }
}
