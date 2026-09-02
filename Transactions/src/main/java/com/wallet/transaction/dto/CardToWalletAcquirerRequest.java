package com.wallet.transaction.dto;

import java.math.BigDecimal;

public class CardToWalletAcquirerRequest {
    private String clientSecret;
    private String channelCode;
    private String fromCardNo;
    private String fromBankName;
    private String fromAccountTitle;
    private String toAccountId;
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

    public String getFromCardNo() { return fromCardNo; }
    public void setFromCardNo(String fromCardNo) { this.fromCardNo = fromCardNo; }

    public String getFromBankName() { return fromBankName; }
    public void setFromBankName(String fromBankName) { this.fromBankName = fromBankName; }

    public String getFromAccountTitle() { return fromAccountTitle; }
    public void setFromAccountTitle(String fromAccountTitle) { this.fromAccountTitle = fromAccountTitle; }

    public String getToAccountId() { return toAccountId; }
    public void setToAccountId(String toAccountId) { this.toAccountId = toAccountId; }

    public BigDecimal getTransAmount() { return transAmount; }
    public void setTransAmount(BigDecimal transAmount) { this.transAmount = transAmount; }

    public Long getAppUserId() { return appUserId; }
    public void setAppUserId(Long appUserId) { this.appUserId = appUserId; }

    public String getStan() { return stan; }
    public void setStan(String stan) { this.stan = stan; }

    public String getRrn() { return rrn; }
    public void setRrn(String rrn) { this.rrn = rrn; }
}