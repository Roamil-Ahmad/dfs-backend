package com.wallet.transaction.dto.aps;

public class BalanceInquiryRqst {
    private String pan;
    private String track2Data;
    private String cardAcceptorIdentification;
    private String cardAcceptorCode;
    private String cvc2Data;
    private String cvcPresent;

    private PointOfService pointOfService;

    private CardAcceptorNameAndLocation cardAcceptorNameAndLocation;

    private String dateExpiration;

    private String currencyCodeTransaction;

    private String pinData;

    private String merchantType;

    private String terminalType;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getTrack2Data() {
        return track2Data;
    }

    public void setTrack2Data(String track2Data) {
        this.track2Data = track2Data;
    }


    public CardAcceptorNameAndLocation getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation(CardAcceptorNameAndLocation cardAcceptorNameAndLocation) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
    }

    public String getCurrencyCodeTransaction() {
        return currencyCodeTransaction;
    }

    public void setCurrencyCodeTransaction(String currencyCodeTransaction) {
        this.currencyCodeTransaction = currencyCodeTransaction;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }


    public String getPinData() {
        return pinData;
    }

    public void setPinData(String pinData) {
        this.pinData = pinData;
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

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getCvc2Data() {
        return cvc2Data;
    }

    public void setCvc2Data(String cvc2Data) {
        this.cvc2Data = cvc2Data;
    }

    public String getCvcPresent() {
        return cvcPresent;
    }

    public void setCvcPresent(String cvcPresent) {
        this.cvcPresent = cvcPresent;
    }
}
