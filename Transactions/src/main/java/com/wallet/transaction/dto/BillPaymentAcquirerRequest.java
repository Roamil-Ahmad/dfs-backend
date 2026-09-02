package com.wallet.transaction.dto;

import java.math.BigDecimal;

public class BillPaymentAcquirerRequest {

    private String clientSecret;
    private String channelCode;
    private String fromCardNo;
    private String fromCardTitle;
    private String utilityCompanyCode;
    private String utilityConsumerNo;
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

    public String getFromCardTitle() { return fromCardTitle; }
    public void setFromCardTitle(String fromCardTitle) { this.fromCardTitle = fromCardTitle; }

    public String getUtilityCompanyCode() { return utilityCompanyCode; }
    public void setUtilityCompanyCode(String utilityCompanyCode) { this.utilityCompanyCode = utilityCompanyCode; }

    public String getUtilityConsumerNo() { return utilityConsumerNo; }
    public void setUtilityConsumerNo(String utilityConsumerNo) { this.utilityConsumerNo = utilityConsumerNo; }

    public BigDecimal getTransAmount() { return transAmount; }
    public void setTransAmount(BigDecimal transAmount) { this.transAmount = transAmount; }

    public Long getAppUserId() { return appUserId; }
    public void setAppUserId(Long appUserId) { this.appUserId = appUserId; }

    public String getStan() { return stan; }
    public void setStan(String stan) { this.stan = stan; }

    public String getRrn() { return rrn; }
    public void setRrn(String rrn) { this.rrn = rrn; }
}