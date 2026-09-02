package com.dfs.switchgateway.dto.TSDtos.responseDto;

/*
Author Name: abdul.fatah

Project Name: switch-gateway

Package Name: com.dfs.switchgateway.dto.TSDtos

Class Name: IBFTAdviceRequest

Date and Time:5/26/2023 12:23 PM

Version:1.0
*/

import java.util.Date;

public class IBFTTitleFetchResponse {

    private String pan;
    private String cardAcceptorNameAndLocation;
    private String cardAcceptorTerminalId;
    private String identifier;
    private String transactionAmount;
    private Date requestTime;
    private Date transmissionTime;
    private String stan;
    private String rrn;
    private String responseCode;
    private String accountTitle;
    private String pointOfEntry;
    private String accountNo1;
    private String accountNo2;

    private String purposeOfPayment;

    private String toBankImd;
    private String merchantType;
    private String networkIdentifier;
    private String message;

    private String dateLocalTransaction;
    private String timeLocalTransaction;
    private String accountBranchName;
    private String accountBankName;
    private String beneficiaryIban;
    private String beneficiaryId;


    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getCardAcceptorNameAndLocation() {
        return cardAcceptorNameAndLocation;
    }

    public void setCardAcceptorNameAndLocation(String cardAcceptorNameAndLocation) {
        this.cardAcceptorNameAndLocation = cardAcceptorNameAndLocation;
    }

    public String getCardAcceptorTerminalId() {
        return cardAcceptorTerminalId;
    }

    public void setCardAcceptorTerminalId(String cardAcceptorTerminalId) {
        this.cardAcceptorTerminalId = cardAcceptorTerminalId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getTransmissionTime() {
        return transmissionTime;
    }

    public void setTransmissionTime(Date transmissionTime) {
        this.transmissionTime = transmissionTime;
    }

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

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getPointOfEntry() {
        return pointOfEntry;
    }

    public void setPointOfEntry(String pointOfEntry) {
        this.pointOfEntry = pointOfEntry;
    }

    public String getAccountNo1() {
        return accountNo1;
    }

    public void setAccountNo1(String accountNo1) {
        this.accountNo1 = accountNo1;
    }

    public String getAccountNo2() {
        return accountNo2;
    }

    public void setAccountNo2(String accountNo2) {
        this.accountNo2 = accountNo2;
    }

    public String getPurposeOfPayment() {
        return purposeOfPayment;
    }

    public void setPurposeOfPayment(String purposeOfPayment) {
        this.purposeOfPayment = purposeOfPayment;
    }

    public String getToBankImd() {
        return toBankImd;
    }

    public void setToBankImd(String toBankImd) {
        this.toBankImd = toBankImd;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getNetworkIdentifier() {
        return networkIdentifier;
    }

    public void setNetworkIdentifier(String networkIdentifier) {
        this.networkIdentifier = networkIdentifier;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateLocalTransaction() {
        return dateLocalTransaction;
    }

    public void setDateLocalTransaction(String dateLocalTransaction) {
        this.dateLocalTransaction = dateLocalTransaction;
    }

    public String getTimeLocalTransaction() {
        return timeLocalTransaction;
    }

    public void setTimeLocalTransaction(String timeLocalTransaction) {
        this.timeLocalTransaction = timeLocalTransaction;
    }

    public String getAccountBranchName() {
        return accountBranchName;
    }

    public void setAccountBranchName(String accountBranchName) {
        this.accountBranchName = accountBranchName;
    }

    public String getAccountBankName() {
        return accountBankName;
    }

    public void setAccountBankName(String accountBankName) {
        this.accountBankName = accountBankName;
    }

    public String getBeneficiaryIban() {
        return beneficiaryIban;
    }

    public void setBeneficiaryIban(String beneficiaryIban) {
        this.beneficiaryIban = beneficiaryIban;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }
}

