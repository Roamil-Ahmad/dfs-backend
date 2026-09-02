package com.mfs.pricingprofile.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mfs.pricingprofile.utils.BigDecimalPlainStringSerializer;

import java.math.BigDecimal;

public class Velocity {

    private long pricingVelocityId;
    @JsonSerialize(using = BigDecimalPlainStringSerializer.class)
    private BigDecimal velocityAmount;
    private Long noOfFreeTrxn;
    private String frequency;
    private String isActive;

    public long getPricingVelocityId() {
        return pricingVelocityId;
    }

    public void setPricingVelocityId(long pricingVelocityId) {
        this.pricingVelocityId = pricingVelocityId;
    }

    public BigDecimal getVelocityAmount() {
        return velocityAmount;
    }

    public void setVelocityAmount(BigDecimal velocityAmount) {
        this.velocityAmount = velocityAmount;
    }

    public Long getNoOfFreeTrxn() {
        return noOfFreeTrxn;
    }

    public void setNoOfFreeTrxn(Long noOfFreeTrxn) {
        this.noOfFreeTrxn = noOfFreeTrxn;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
