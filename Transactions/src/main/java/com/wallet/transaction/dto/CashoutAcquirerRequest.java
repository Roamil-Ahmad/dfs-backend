package com.wallet.transaction.dto;

import java.math.BigDecimal;

public class CashoutAcquirerRequest {
    private String fromCardNo;
    private String fromBankName;
    private String fromAccountTitle;
    private String toAccountId;
    private BigDecimal transAmount;
    private Long appUserId;
    private String stan;
    private String rrn;
    private String province;
    private String ditrict;

    // Getters and setters
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDitrict() {
        return ditrict;
    }

    public void setDitrict(String ditrict) {
        this.ditrict = ditrict;
    }
}