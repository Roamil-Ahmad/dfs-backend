package com.wallet.transaction.dto;

import java.math.BigDecimal;

public class PurchaseAcquirerRequest {
    private Long toAccountId;
    private String fromCardNo;
    private String fromBankName;
    private String fromAccountTitle;
    private BigDecimal transAmount;
    private Long appUserId;
    private String stan;
    private String rrn;
    private String cardAcceptorNameLocation;
    private String cardAcceptorTerminalId;

    // Getters and setters
    public Long getToAccountId() { return toAccountId; }
    public void setToAccountId(Long toAccountId) { this.toAccountId = toAccountId; }

    public String getFromCardNo() { return fromCardNo; }
    public void setFromCardNo(String fromCardNo) { this.fromCardNo = fromCardNo; }

    public String getFromBankName() { return fromBankName; }
    public void setFromBankName(String fromBankName) { this.fromBankName = fromBankName; }

    public String getFromAccountTitle() { return fromAccountTitle; }
    public void setFromAccountTitle(String fromAccountTitle) { this.fromAccountTitle = fromAccountTitle; }

    public BigDecimal getTransAmount() { return transAmount; }
    public void setTransAmount(BigDecimal transAmount) { this.transAmount = transAmount; }

    public Long getAppUserId() { return appUserId; }
    public void setAppUserId(Long appUserId) { this.appUserId = appUserId; }

    public String getStan() { return stan; }
    public void setStan(String stan) { this.stan = stan; }

    public String getRrn() { return rrn; }
    public void setRrn(String rrn) { this.rrn = rrn; }

    public String getCardAcceptorNameLocation() { return cardAcceptorNameLocation; }
    public void setCardAcceptorNameLocation(String cardAcceptorNameLocation) { this.cardAcceptorNameLocation = cardAcceptorNameLocation; }

    public String getCardAcceptorTerminalId() { return cardAcceptorTerminalId; }
    public void setCardAcceptorTerminalId(String cardAcceptorTerminalId) { this.cardAcceptorTerminalId = cardAcceptorTerminalId; }
}