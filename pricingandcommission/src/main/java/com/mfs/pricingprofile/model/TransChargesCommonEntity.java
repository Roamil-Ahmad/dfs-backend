package com.mfs.pricingprofile.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@MappedSuperclass
public class TransChargesCommonEntity implements Serializable {

    private Date createdate;

    private BigDecimal createuser;

    @Column(name = "IS_ACTIVE")
    private String isActive;

    private Date lastupdatedate;

    private BigDecimal lastupdateuser;

    private BigDecimal updateindex;

    //bi-directional many-to-one association to TblCmsWhitelistDevice
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "TRANS_CHARGES_ID")
    private TblTransCharge tblTransCharge;

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public BigDecimal getCreateuser() {
        return createuser;
    }

    public void setCreateuser(BigDecimal createuser) {
        this.createuser = createuser;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Date getLastupdatedate() {
        return lastupdatedate;
    }

    public void setLastupdatedate(Date lastupdatedate) {
        this.lastupdatedate = lastupdatedate;
    }

    public BigDecimal getLastupdateuser() {
        return lastupdateuser;
    }

    public void setLastupdateuser(BigDecimal lastupdateuser) {
        this.lastupdateuser = lastupdateuser;
    }

    public BigDecimal getUpdateindex() {
        return updateindex;
    }

    public void setUpdateindex(BigDecimal updateindex) {
        this.updateindex = updateindex;
    }

    public TblTransCharge getTblTransCharge() {
        return tblTransCharge;
    }

    public void setTblTransCharge(TblTransCharge tblTransCharge) {
        this.tblTransCharge = tblTransCharge;
    }
}
