package com.wallet.transaction.dto.aps;

public class CardToCardCpRqst {

    private String pan;
    private String merchantType;
    private String amount;
    private String tract2Data;
    private String pin;
    private String cardAcceptorIdentification;
    private String cardAcceptorCode;
    private PointOfService pointOfService;
    private CardAcceptorNameAndLocation cardAcceptorNameAndLocation;
    private String cvcPresent;
    private String cvc2;
    private String transactionType;
    private String terminalType;
    private String card2Number;
    private String currencyCodeTransaction;
    private String currencyCodeCardBilling;
    private String dateExpiration;
    private String otpPin;
    private String originalRrn;
    private String stan;
    private String rrn;
    private String networkReferenceNumber;

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

    public String getCurrencyCodeTransaction() {
        return currencyCodeTransaction;
    }

    public void setCurrencyCodeTransaction(String currencyCodeTransaction) {
        this.currencyCodeTransaction = currencyCodeTransaction;
    }

    public String getCurrencyCodeCardBilling() {
        return currencyCodeCardBilling;
    }

    public void setCurrencyCodeCardBilling(String currencyCodeCardBilling) {
        this.currencyCodeCardBilling = currencyCodeCardBilling;
    }

    public String getCard2Number() {
        return card2Number;
    }

    public void setCard2Number(String card2Number) {
        this.card2Number = card2Number;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getCardAcceptorIdentification() {
        return cardAcceptorIdentification;
    }

    public void setCardAcceptorIdentification(String cardAcceptorIdentification) {
        this.cardAcceptorIdentification = cardAcceptorIdentification;
    }

    public String getCardAcceptorCode() {
        return cardAcceptorCode;
    }

    public void setCardAcceptorCode(String cardAcceptorCode) {
        this.cardAcceptorCode = cardAcceptorCode;
    }

    public PointOfService getPointOfService() {
        return pointOfService;
    }

    public void setPointOfService(PointOfService pointOfService) {
        this.pointOfService = pointOfService;
    }

    public CardAcceptorNameAndLocation getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation(CardAcceptorNameAndLocation cardAcceptorNameAndLocation) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
    }

    public String getCvcPresent() {
        return cvcPresent;
    }

    public void setCvcPresent(String cvcPresent) {
        this.cvcPresent = cvcPresent;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTract2Data() {
        return tract2Data;
    }

    public void setTract2Data(String tract2Data) {
        this.tract2Data = tract2Data;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCvc2() {
        return cvc2;
    }

    public void setCvc2(String cvc2) {
        this.cvc2 = cvc2;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getOtpPin() {
        return otpPin;
    }

    public void setOtpPin(String otpPin) {
        this.otpPin = otpPin;
    }

    public String getOriginalRrn() {
        return originalRrn;
    }

    public void setOriginalRrn(String originalRrn) {
        this.originalRrn = originalRrn;
    }

    public String getNetworkReferenceNumber() {
        return networkReferenceNumber;
    }

    public void setNetworkReferenceNumber(String networkReferenceNumber) {
        this.networkReferenceNumber = networkReferenceNumber;
    }
}
