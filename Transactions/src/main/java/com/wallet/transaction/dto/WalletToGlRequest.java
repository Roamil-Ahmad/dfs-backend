package com.wallet.transaction.dto;

import java.math.BigDecimal;

public class WalletToGlRequest {
    private Long fromAccountId;
    private Long toGlAccountId;
    private BigDecimal transAmount;
    private Long appUserId;

    // Getters and setters
    public Long getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }

    public Long getToGlAccountId() { return toGlAccountId; }
    public void setToGlAccountId(Long toGlAccountId) { this.toGlAccountId = toGlAccountId; }

    public BigDecimal getTransAmount() { return transAmount; }
    public void setTransAmount(BigDecimal transAmount) { this.transAmount = transAmount; }

    public Long getAppUserId() { return appUserId; }
    public void setAppUserId(Long appUserId) { this.appUserId = appUserId; }
}