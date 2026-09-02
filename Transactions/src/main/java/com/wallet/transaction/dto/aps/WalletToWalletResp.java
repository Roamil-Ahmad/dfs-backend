package com.wallet.transaction.dto.aps;


public class WalletToWalletResp {
    private String stan;
    private String timeLocalTransaction;
    private String retrievalReferenceNumber;
    private String responseCode;
    private String additionalDataPrivate;
    private String currencyCode;
    private String additionalAmount;
    private String authorizationIdentificationResponse;
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

    public String getTimeLocalTransaction() {
        return timeLocalTransaction;
    }

    public void setTimeLocalTransaction(String timeLocalTransaction) {
        this.timeLocalTransaction = timeLocalTransaction;
    }

    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getAdditionalDataPrivate() {
        return additionalDataPrivate;
    }

    public void setAdditionalDataPrivate(String additionalDataPrivate) {
        this.additionalDataPrivate = additionalDataPrivate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(String additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public String getAuthorizationIdentificationResponse() {
        return authorizationIdentificationResponse;
    }

    public void setAuthorizationIdentificationResponse(String authorizationIdentificationResponse) {
        this.authorizationIdentificationResponse = authorizationIdentificationResponse;
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
