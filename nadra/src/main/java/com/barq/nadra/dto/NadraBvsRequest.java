package com.barq.nadra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
@SuppressWarnings({"java:S116","java:S117","java:S100"})
public class NadraBvsRequest {
    private String processingCode;
    private String dateTime;
    private String traceNo;
    private String merchantType;
    private String companyName;
    @JsonProperty("SessionId")
    private Object sessionId;
    @JsonProperty("TransactionId")
    private String transactionId;
    @JsonProperty("RemittanceAmount")
    private Object remittanceAmount;
    @JsonProperty("RemittanceType")
    private Object remittanceType;
    @JsonProperty("CitizenNumber")
    private String citizenNumber;
    @JsonProperty("AccountNumber")
    private Object accountNumber;
    @JsonProperty("AccountLevel")
    private Object accountLevel;
    @JsonProperty("ContactNumber")
    private String contactNumber;
    @JsonProperty("FingerIndex")
    private String fingerIndex;
    @JsonProperty("FingerTemplate")
    private String fingerTemplate;
    @JsonProperty("TemplateType")
    private String templateType;
    @JsonProperty("SecondaryCitizenNumber")
    private String secondaryCitizenNumber;
    @JsonProperty("SecondaryContactNumber")
    private String secondaryContactNumber;
    @JsonProperty("AreaName")
    private String areaName;

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Object getSessionId() {
        return sessionId;
    }

    public void setSessionId(Object sessionId) {
        this.sessionId = sessionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Object getRemittanceAmount() {
        return remittanceAmount;
    }

    public void setRemittanceAmount(Object remittanceAmount) {
        this.remittanceAmount = remittanceAmount;
    }

    public Object getRemittanceType() {
        return remittanceType;
    }

    public void setRemittanceType(Object remittanceType) {
        this.remittanceType = remittanceType;
    }

    public String getCitizenNumber() {
        return citizenNumber;
    }

    public void setCitizenNumber(String citizenNumber) {
        this.citizenNumber = citizenNumber;
    }

    public Object getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Object accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Object getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(Object accountLevel) {
        this.accountLevel = accountLevel;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getFingerIndex() {
        return fingerIndex;
    }

    public void setFingerIndex(String fingerIndex) {
        this.fingerIndex = fingerIndex;
    }

    public String getFingerTemplate() {
        return fingerTemplate;
    }

    public void setFingerTemplate(String fingerTemplate) {
        this.fingerTemplate = fingerTemplate;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getSecondaryCitizenNumber() {
        return secondaryCitizenNumber;
    }

    public void setSecondaryCitizenNumber(String secondaryCitizenNumber) {
        this.secondaryCitizenNumber = secondaryCitizenNumber;
    }

    public String getSecondaryContactNumber() {
        return secondaryContactNumber;
    }

    public void setSecondaryContactNumber(String secondaryContactNumber) {
        this.secondaryContactNumber = secondaryContactNumber;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
