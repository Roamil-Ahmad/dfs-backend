package com.dfs.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the LKP_CMS_CATEGORY database table.
 */
@Entity
@Table(name = "LKP_CMS_CATEGORY")
@NamedQuery(name = "LkpCmsCategory.findAll", query = "SELECT l FROM LkpCmsCategory l")
public class LkpCmsCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "LKP_CMS_CATEGORY_CMSCATEGORYID_GENERATOR", sequenceName = "LKP_CMS_CATEGORY_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LKP_CMS_CATEGORY_CMSCATEGORYID_GENERATOR")
    @Column(name = "CMS_CATEGORY_ID")
    private long cmsCategoryId;

    @Column(name = "CMS_CATEGORY_CODE")
    private String cmsCategoryCode;

    @Column(name = "CMS_CATEGORY_DESCR")
    private String cmsCategoryDescr;
    @JsonIgnore
    private Timestamp createdate;
    @JsonIgnore
    private BigDecimal createuser;

    @Column(name = "IS_ACTIVE")
    private String isActive;
    @JsonIgnore
    private Timestamp lastupdatedate;

    @JsonIgnore
    private BigDecimal lastupdateuser;

    @Column(name = "STATUS_ID")
    @JsonIgnore
    private BigDecimal statusId;

    @JsonIgnore
    private BigDecimal updateindex;

    //bi-directional many-to-one association to TblCmsMenu
    @OneToMany(mappedBy = "lkpCmsCategory")
    private List<TblCmsMenu> tblCmsMenus;

    public long getCmsCategoryId() {
        return this.cmsCategoryId;
    }

    public void setCmsCategoryId(long cmsCategoryId) {
        this.cmsCategoryId = cmsCategoryId;
    }

    public String getCmsCategoryCode() {
        return this.cmsCategoryCode;
    }

    public void setCmsCategoryCode(String cmsCategoryCode) {
        this.cmsCategoryCode = cmsCategoryCode;
    }

    public String getCmsCategoryDescr() {
        return this.cmsCategoryDescr;
    }

    public void setCmsCategoryDescr(String cmsCategoryDescr) {
        this.cmsCategoryDescr = cmsCategoryDescr;
    }

    public Timestamp getCreatedate() {
        return this.createdate;
    }

    public void setCreatedate(Timestamp createdate) {
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

    public Timestamp getLastupdatedate() {
        return this.lastupdatedate;
    }

    public void setLastupdatedate(Timestamp lastupdatedate) {
        this.lastupdatedate = lastupdatedate;
    }

    public BigDecimal getLastupdateuser() {
        return this.lastupdateuser;
    }

    public void setLastupdateuser(BigDecimal lastupdateuser) {
        this.lastupdateuser = lastupdateuser;
    }

    public BigDecimal getStatusId() {
        return this.statusId;
    }

    public void setStatusId(BigDecimal statusId) {
        this.statusId = statusId;
    }

    public BigDecimal getUpdateindex() {
        return this.updateindex;
    }

    public void setUpdateindex(BigDecimal updateindex) {
        this.updateindex = updateindex;
    }

    public List<TblCmsMenu> getTblCmsMenus() {
        return this.tblCmsMenus;
    }

    public void setTblCmsMenus(List<TblCmsMenu> tblCmsMenus) {
        this.tblCmsMenus = tblCmsMenus;
    }

    public TblCmsMenu addTblCmsMenus(TblCmsMenu tblCmsMenus) {
        getTblCmsMenus().add(tblCmsMenus);
        tblCmsMenus.setLkpCmsCategory(this);

        return tblCmsMenus;
    }

    public TblCmsMenu removeTblCmsMenus(TblCmsMenu tblCmsMenus) {
        getTblCmsMenus().remove(tblCmsMenus);
        tblCmsMenus.setLkpCmsCategory(null);

        return tblCmsMenus;
    }

}