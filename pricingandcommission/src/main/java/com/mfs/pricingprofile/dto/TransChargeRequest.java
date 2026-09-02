/*Author Name:ahmed.sufyan

Project Name: Configurations

Package Name:com.mfs.configurations.dto

Class Name: TblTransChargeRequest

Date and Time:5/18/2023 11:45 AM

Version:1.0*/
package com.mfs.pricingprofile.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TransChargeRequest {
    private long transChargesId;
    private BigDecimal statusId;
    private String chargesApplicable;
    private String chargesInclExcl;
    private String chargesProfileName;
    private Date effectiveFrom;
    private Date effectiveTo;
    private String fedInclExcl;
    private String isActive;
    private BigDecimal velocityAmount;
    private String velocityFrequency;
    private Long channelId;
    private Long glAccountId;
    private List<Long> transChargesDocsId;
    private List<Long> transChargesChannelsId;
    private List<Long> transChargesSegmentsId;
    private List<TransChargesSlabRequest> tblTransChargesSlabs;

    public long getTransChargesId() {
        return transChargesId;
    }

    public void setTransChargesId(long transChargesId) {
        this.transChargesId = transChargesId;
    }

    public BigDecimal getStatusId() {
        return statusId;
    }

    public void setStatusId(BigDecimal statusId) {
        this.statusId = statusId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getChargesApplicable() {
        return chargesApplicable;
    }

    public void setChargesApplicable(String chargesApplicable) {
        this.chargesApplicable = chargesApplicable;
    }

    public String getChargesInclExcl() {
        return chargesInclExcl;
    }

    public void setChargesInclExcl(String chargesInclExcl) {
        this.chargesInclExcl = chargesInclExcl;
    }

    public String getChargesProfileName() {
        return chargesProfileName;
    }

    public void setChargesProfileName(String chargesProfileName) {
        this.chargesProfileName = chargesProfileName;
    }

    public Date getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(Date effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public Date getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(Date effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public String getFedInclExcl() {
        return fedInclExcl;
    }

    public void setFedInclExcl(String fedInclExcl) {
        this.fedInclExcl = fedInclExcl;
    }

    public BigDecimal getVelocityAmount() {
        return velocityAmount;
    }

    public void setVelocityAmount(BigDecimal velocityAmount) {
        this.velocityAmount = velocityAmount;
    }

    public String getVelocityFrequency() {
        return velocityFrequency;
    }

    public void setVelocityFrequency(String velocityFrequency) {
        this.velocityFrequency = velocityFrequency;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getGlAccountId() {
        return glAccountId;
    }

    public void setGlAccountId(Long glAccountId) {
        this.glAccountId = glAccountId;
    }

    public List<Long> getTransChargesDocsId() {
        return transChargesDocsId;
    }

    public void setTransChargesDocsId(List<Long> transChargesDocsId) {
        this.transChargesDocsId = transChargesDocsId;
    }

    public List<Long> getTransChargesChannelsId() {
        return transChargesChannelsId;
    }

    public void setTransChargesChannelsId(List<Long> transChargesChannelsId) {
        this.transChargesChannelsId = transChargesChannelsId;
    }

    public List<Long> getTransChargesSegmentsId() {
        return transChargesSegmentsId;
    }

    public void setTransChargesSegmentsId(List<Long> transChargesSegmentsId) {
        this.transChargesSegmentsId = transChargesSegmentsId;
    }

    public List<TransChargesSlabRequest> getTblTransChargesSlabs() {
        return tblTransChargesSlabs;
    }

    public void setTblTransChargesSlabs(List<TransChargesSlabRequest> tblTransChargesSlabs) {
        this.tblTransChargesSlabs = tblTransChargesSlabs;
    }
}


