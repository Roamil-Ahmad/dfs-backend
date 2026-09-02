/*Author Name:ahmed.sufyan

Project Name: Configurations

Package Name:com.mfs.configurations.dto

Class Name: SaveTblCommissionProfileRequest

Date and Time:5/19/2023 12:20 PM

Version:1.0*/
package com.mfs.pricingprofile.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mfs.pricingprofile.utils.BigDecimalPlainStringSerializer;

import java.math.BigDecimal;

public class CommissionSlabRequest {

    private long commissionSlabId;
    @JsonSerialize(using = BigDecimalPlainStringSerializer.class)
    private BigDecimal commissionAmount;
    @JsonSerialize(using = BigDecimalPlainStringSerializer.class)
    private BigDecimal commissionPercentage;
    @JsonSerialize(using = BigDecimalPlainStringSerializer.class)
    private BigDecimal toAmount;
    @JsonSerialize(using = BigDecimalPlainStringSerializer.class)
    private BigDecimal fromAmount;

    private String commissionTypeCode;

    private String isActive;

    public long getCommissionSlabId() {
        return commissionSlabId;
    }

    public void setCommissionSlabId(long commissionSlabId) {
        this.commissionSlabId = commissionSlabId;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public BigDecimal getCommissionPercentage() {
        return commissionPercentage;
    }

    public void setCommissionPercentage(BigDecimal commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public String getCommissionTypeCode() {
        return commissionTypeCode;
    }

    public void setCommissionTypeCode(String commissionTypeCode) {
        this.commissionTypeCode = commissionTypeCode;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}


