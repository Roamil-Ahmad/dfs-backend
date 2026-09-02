package com.mfs.pricingprofile.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mfs.pricingprofile.utils.BigDecimalPlainStringSerializer;

import java.math.BigDecimal;

public class TransChargesSlabRequest {

    private long transChargesSlabId;
    @JsonSerialize(using = BigDecimalPlainStringSerializer.class)
    private BigDecimal fromAmount;
    @JsonSerialize(using = BigDecimalPlainStringSerializer.class)
    private BigDecimal toAmount;
    @JsonSerialize(using = BigDecimalPlainStringSerializer.class)
    private BigDecimal oneLinkShare;
    private String oneLinkShareType;
    private String feeCondition;
    @JsonSerialize(using = BigDecimalPlainStringSerializer.class)
    private BigDecimal amount;
    @JsonSerialize(using = BigDecimalPlainStringSerializer.class)
    private BigDecimal percentage;
    private String oprtr;

    public long getTransChargesSlabId() {
        return transChargesSlabId;
    }

    public void setTransChargesSlabId(long transChargesSlabId) {
        this.transChargesSlabId = transChargesSlabId;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }

    public BigDecimal getOneLinkShare() {
        return oneLinkShare;
    }

    public void setOneLinkShare(BigDecimal oneLinkShare) {
        this.oneLinkShare = oneLinkShare;
    }

    public String getOneLinkShareType() {
        return oneLinkShareType;
    }

    public void setOneLinkShareType(String oneLinkShareType) {
        this.oneLinkShareType = oneLinkShareType;
    }

    public String getFeeCondition() {
        return feeCondition;
    }

    public void setFeeCondition(String feeCondition) {
        this.feeCondition = feeCondition;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public String getOprtr() {
        return oprtr;
    }

    public void setOprtr(String oprtr) {
        this.oprtr = oprtr;
    }
}
