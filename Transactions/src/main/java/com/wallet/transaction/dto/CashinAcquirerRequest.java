package com.wallet.transaction.dto;

import java.math.BigDecimal;

public class CashinAcquirerRequest {
    private String toCardNo;
    private String toBankName;
    private String toAccountTitle;
    private String fromAccountId;
    private BigDecimal transAmount;
    private Long appUserId;
    private String stan;
    private String rrn;
    private String province;
    private String ditrict;

    // Getters and setters
    public String getToCardNo() { return toCardNo; }
    public void setToCardNo(String toCardNo) { this.toCardNo = toCardNo; }

    public String getToBankName() { return toBankName; }
    public void setToBankName(String toBankName) { this.toBankName = toBankName; }

    public String getToAccountTitle() { return toAccountTitle; }
    public void setToAccountTitle(String toAccountTitle) { this.toAccountTitle = toAccountTitle; }

    public String getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(String fromAccountId) { this.fromAccountId = fromAccountId; }

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