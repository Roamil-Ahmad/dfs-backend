package com.wallet.transaction.dto.aps;

public class SavePaymentRqst {

    private String pan;
    private String merchantType;
    private String cardAcceptorIdentification;
    private String cardAcceptorCode;
    private String amount;
    private PointOfService pointOfService;
    private CardAcceptorNameAndLocation cardAcceptorNameAndLocation;
    private String cvcPresent;
    private String transactionType;
    private String transactionCurrencyCode;
    private String terminalType;
    private String serviceId;
    private String billRefNo;
    private String billAmount;
    private String dateExpiration;
    private String cvc2;
    private String requestId;
    private String otpPin;
    private String originalRrn;
    private String stan;
    private String rrn;
    private String pin;
    private String track2Data;

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

    public String getTransactionCurrencyCode() {
        return transactionCurrencyCode;
    }

    public void setTransactionCurrencyCode(String transactionCurrencyCode) {
        this.transactionCurrencyCode = transactionCurrencyCode;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getBillRefNo() {
        return billRefNo;
    }

    public void setBillRefNo(String billRefNo) {
        this.billRefNo = billRefNo;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCvc2() {
        return cvc2;
    }

    public void setCvc2(String cvc2) {
        this.cvc2 = cvc2;
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTrack2Data() {
        return track2Data;
    }

    public void setTrack2Data(String track2Data) {
        this.track2Data = track2Data;
    }
}
