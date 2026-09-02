package com.wallet.transaction.dto;

import java.math.BigDecimal;

public class CashoutIssuerRequest {
    private String clientSecret;
    private String channelCode;
    private String fromCardNo;
    private String toCardNo;
    private String toBankName;
    private String toAccountTitle;
    private BigDecimal transAmount;
    private Long appUserId;
    private String stan;
    private String rrn;

    // Getters and setters

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getFromCardNo() {
        return fromCardNo;
    }

    public void setFromCardNo(String fromCardNo) {
        this.fromCardNo = fromCardNo;
    }

    public String getToCardNo() { return toCardNo; }
    public void setToCardNo(String toCardNo) { this.toCardNo = toCardNo; }

    public String getToBankName() { return toBankName; }
    public void setToBankName(String toBankName) { this.toBankName = toBankName; }

    public String getToAccountTitle() { return toAccountTitle; }
    public void setToAccountTitle(String toAccountTitle) { this.toAccountTitle = toAccountTitle; }

    public BigDecimal getTransAmount() { return transAmount; }
    public void setTransAmount(BigDecimal transAmount) { this.transAmount = transAmount; }

    public Long getAppUserId() { return appUserId; }
    public void setAppUserId(Long appUserId) { this.appUserId = appUserId; }

    public String getStan() { return stan; }
    public void setStan(String stan) { this.stan = stan; }

    public String getRrn() { return rrn; }
    public void setRrn(String rrn) { this.rrn = rrn; }
}