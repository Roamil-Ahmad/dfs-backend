package com.wallet.transaction.dto.aps;

public class SavePaymentResp {

    private String stan;
    private String timeLocalTransaction;

    private String retrievalReferenceNumber;

    private String cardAcceptorTerminalIdentification;

    private String responseCode;

    private String additionalDataPrivate;
    private String billNumber;
    private String billAmount;
    private String billRefNo;
    private String billRecieptNo;
    private String availableBalance;
    private String currencyAvailable;
    private String fee;



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



    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
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

    public String getBillRefNo() {
        return billRefNo;
    }

    public void setBillRefNo(String billRefNo) {
        this.billRefNo = billRefNo;
    }

    public String getBillRecieptNo() {
        return billRecieptNo;
    }

    public void setBillRecieptNo(String billRecieptNo) {
        this.billRecieptNo = billRecieptNo;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
