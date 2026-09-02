package com.wallet.transaction.dto.aps;

public class SavePaymentInfoRqst {

    private String pan;
    private String merchantType;
    private String cardAcceptorIdentification;
    private String cardAcceptorCode;
    private PointOfService pointOfService;
    private CardAcceptorNameAndLocation cardAcceptorNameAndLocation;
    private String cvcPresent;
    private String transactionType;
    private String transactionCurrencyCode;
    private String terminalType;
    private String serviceId;
    private String billRefNo;

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
}
