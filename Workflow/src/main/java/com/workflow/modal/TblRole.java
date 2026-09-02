package com.workflow.modal;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_ROLE database table.
 */
@Entity
@Table(name = "TBL_ROLE")
@NamedQuery(name = "TblRole.findAll", query = "SELECT t FROM TblRole t")
public class TblRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TBL_ROLE_ROLEID_GENERATOR", sequenceName = "TBL_ROLE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_ROLE_ROLEID_GENERATOR")
    @Column(name = "ROLE_ID")
    private long roleId;

    private Date createdate;

    private BigDecimal createuser;

    @Column(name = "IS_ACTIVE")
    private String isActive;

    private Date lastupdatedate;

    private BigDecimal lastupdateuser;

    @Column(name = "ROLE_CODE")
    private String roleCode;

    @Column(name = "ROLE_DESCR")
    private String roleDescr;

    private BigDecimal updateindex;

    //bi-directional many-to-one association to LkpStatus
    @ManyToOne
    @JoinColumn(name = "STATUS_ID")
    private LkpStatus lkpStatus;

    public long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public Date getCreatedate() {
        return this.createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public BigDecimal getCreateuser() {
        return this.createuser;
    }

    public void setCreateuser(BigDecimal createuser) {
        this.createuser = createuser;
    }

    public String getIsActive() {
        return this.isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Date getLastupdatedate() {
        return this.lastupdatedate;
    }

    public void setLastupdatedate(Date lastupdatedate) {
        this.lastupdatedate = lastupdatedate;
    }

    public BigDecimal getLastupdateuser() {
        return this.lastupdateuser;
    }

    public void setLastupdateuser(BigDecimal lastupdateuser) {
        this.lastupdateuser = lastupdateuser;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleDescr() {
        return this.roleDescr;
    }

    public void setRoleDescr(String roleDescr) {
        this.roleDescr = roleDescr;
    }

    public BigDecimal getUpdateindex() {
        return this.updateindex;
    }

    public void setUpdateindex(BigDecimal updateindex) {
        this.updateindex = updateindex;
    }

    public LkpStatus getLkpStatus() {
        return this.lkpStatus;
    }

    public void setLkpStatus(LkpStatus lkpStatus) {
        this.lkpStatus = lkpStatus;
    }
}