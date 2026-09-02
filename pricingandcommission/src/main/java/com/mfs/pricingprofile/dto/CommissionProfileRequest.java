/*Author Name:ahmed.sufyan

Project Name: Configurations

Package Name:com.mfs.configurations.dto

Class Name: SaveTblCommissionProfileRequest

Date and Time:5/19/2023 12:20 PM

Version:1.0*/
package com.mfs.pricingprofile.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CommissionProfileRequest {

    private Long commissionProfileId;

    private String commissionProfileName;

    private Date effectiveFrom;

    private Date effectiveTo;

    private Long glAccountId;

    private String isActive;

    private BigDecimal statusId;

    List<Long> tblTransDoc;

    List<CommissionSlabRequest> commissionSlabRequests;

    public Long getCommissionProfileId() {
        return commissionProfileId;
    }

    public void setCommissionProfileId(Long commissionProfileId) {
        this.commissionProfileId = commissionProfileId;
    }

    public String getCommissionProfileName() {
        return commissionProfileName;
    }

    public void setCommissionProfileName(String commissionProfileName) {
        this.commissionProfileName = commissionProfileName;
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

    public Long getGlAccountId() {
        return glAccountId;
    }

    public void setGlAccountId(Long glAccountId) {
        this.glAccountId = glAccountId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public BigDecimal getStatusId() {
        return statusId;
    }

    public void setStatusId(BigDecimal statusId) {
        this.statusId = statusId;
    }

    public List<Long> getTblTransDoc() {
        return tblTransDoc;
    }

    public void setTblTransDoc(List<Long> tblTransDoc) {
        this.tblTransDoc = tblTransDoc;
    }

    public List<CommissionSlabRequest> getCommissionSlabRequests() {
        return commissionSlabRequests;
    }

    public void setCommissionSlabRequests(List<CommissionSlabRequest> commissionSlabRequests) {
        this.commissionSlabRequests = commissionSlabRequests;
    }
}


