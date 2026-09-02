package com.dfs.switchgateway.dto.TSDtos;

/*
Author Name: abdul.fatah

Project Name: switch-gateway

Package Name: com.dfs.switchgateway.dto.TSDtos

Class Name: OTIBFTTitleFetchRequest

Date and Time:7/18/2023 3:40 PM

Version:1.0
*/
public class OTIBFTTitleFetchRequest {

    String pan;
    String transactionAmount;
    String transactionDateTime;
    String merchantType;
    String pointOfEntry;
    String networkIdentifier;
    String cardAcceptorTerminalId;
    String cardAcceptorIdentificationCode;
    String cardAcceptorNameAndLocation;
    String purposeOfPayment;
    String currencyCode;
    String accountNo1;
    String accountNo2;
    String toBankImd;
    String stan;
    String rrn;

    public String getPan() {
        return pan;
    }

    public void setPan( String pan ) {
        this.pan = pan;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount( String transactionAmount ) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime( String transactionDateTime ) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType( String merchantType ) {
        this.merchantType = merchantType;
    }

    public String getPointOfEntry() {
        return pointOfEntry;
    }

    public void setPointOfEntry( String pointOfEntry ) {
        this.pointOfEntry = pointOfEntry;
    }

    public String getNetworkIdentifier() {
        return networkIdentifier;
    }

    public void setNetworkIdentifier( String networkIdentifier ) {
        this.networkIdentifier = networkIdentifier;
    }

    public String getCardAcceptorTerminalId() {
        return cardAcceptorTerminalId;
    }

    public void setCardAcceptorTerminalId( String cardAcceptorTerminalId ) {
        this.cardAcceptorTerminalId = cardAcceptorTerminalId;
    }

    public String getCardAcceptorIdentificationCode() {
        return cardAcceptorIdentificationCode;
    }

    public void setCardAcceptorIdentificationCode( String cardAcceptorIdentificationCode ) {
        this.cardAcceptorIdentificationCode = cardAcceptorIdentificationCode;
    }

    public String getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation( String cardAcceptorNameAndLocation ) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
    }

    public String getPurposeOfPayment() {
        return purposeOfPayment;
    }

    public void setPurposeOfPayment( String purposeOfPayment ) {
        this.purposeOfPayment = purposeOfPayment;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode( String currencyCode ) {
        this.currencyCode = currencyCode;
    }

    public String getAccountNo1() {
        return accountNo1;
    }

    public void setAccountNo1( String accountNo1 ) {
        this.accountNo1 = accountNo1;
    }

    public String getAccountNo2() {
        return accountNo2;
    }

    public void setAccountNo2( String accountNo2 ) {
        this.accountNo2 = accountNo2;
    }

    public String getToBankImd() {
        return toBankImd;
    }

    public void setToBankImd( String toBankImd ) {
        this.toBankImd = toBankImd;
    }

    public String getStan() {
        return stan;
    }

    public void setStan( String stan ) {
        this.stan = stan;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn( String rrn ) {
        this.rrn = rrn;
    }
}
