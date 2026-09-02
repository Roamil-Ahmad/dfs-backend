package com.wallet.transaction.dto.aps;


public class CardTitleFetchResp {

    private String stan;
    private String timeLocalTransaction;

    private String retrievalReferenceNumber;

    private String cardAcceptorTerminalIdentification;

    private String responseCode;



    private String additionalDataPrivate;


    private String cardTitle;

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

    public String getCardAcceptorTerminalIdentification() {
        return cardAcceptorTerminalIdentification;
    }

    public void setCardAcceptorTerminalIdentification(String cardAcceptorTerminalIdentification) {
        this.cardAcceptorTerminalIdentification = cardAcceptorTerminalIdentification;
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

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }



}
