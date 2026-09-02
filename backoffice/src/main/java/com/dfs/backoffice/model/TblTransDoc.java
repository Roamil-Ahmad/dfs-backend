package com.dfs.backoffice.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the TBL_TRANS_DOCS database table.
 */
@Entity
@Table(name = "TBL_TRANS_DOCS")
@NamedQuery(name = "TblTransDoc.findAll", query = "SELECT t FROM TblTransDoc t")
public class TblTransDoc implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TBL_TRANS_DOCS_TRANSDOCSID_GENERATOR", sequenceName = "TBL_TRANS_DOCS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_TRANS_DOCS_TRANSDOCSID_GENERATOR")
    @Column(name = "TRANS_DOCS_ID")
    private long transDocsId;

    private Date createdate;

    private BigDecimal createuser;

    @Column(name = "IS_ACTIVE")
    private String isActive;

    private Date lastupdatedate;

    private BigDecimal lastupdateuser;

    @Column(name = "LIMIT_YN")
    private String limitYn;

    @Column(name = "TRANS_DOCS_CODE")
    private String transDocsCode;

    @Column(name = "TRANS_DOCS_DESCR")
    private String transDocsDescr;

    private BigDecimal updateindex;

    //bi-directional many-to-one association to LkpStatus
    @ManyToOne
    @JoinColumn(name = "STATUS_ID")
    private LkpStatus lkpStatus;

    @Column(name = "TRANS_TYPE_ID")
    private BigDecimal transTypeId;

    @Column(name = "GL_ACCOUNT_ID")
    private BigDecimal glAccountId;

    @Column(name = "TAX_REGIME_ID")
    private BigDecimal taxRegimeId;

    @Column(name = "DAILY_AMT_LIMIT_CR")
    private BigDecimal dailyAmtLimitCr;

    @Column(name="DAILY_AMT_LIMIT_DR")
    private BigDecimal dailyAmtLimitDr;

    @Column(name="MONTHLY_AMT_LIMIT_CR")
    private BigDecimal monthlyAmtLimitCr;

    @Column(name="MONTHLY_AMT_LIMIT_DR")
    private BigDecimal monthlyAmtLimitDr;

    @Column(name="END_POINT")
    private String endPoint;

    public long getTransDocsId() {
        return this.transDocsId;
    }

    public void setTransDocsId(long transDocsId) {
        this.transDocsId = transDocsId;
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

    public BigDecimal getLastupdateuser() {
        return this.lastupdateuser;
    }

    public void setLastupdateuser(BigDecimal lastupdateuser) {
        this.lastupdateuser = lastupdateuser;
    }

    public String getLimitYn() {
        return this.limitYn;
    }

    public void setLimitYn(String limitYn) {
        this.limitYn = limitYn;
    }

    public String getTransDocsCode() {
        return this.transDocsCode;
    }

    public void setTransDocsCode(String transDocsCode) {
        this.transDocsCode = transDocsCode;
    }

    public String getTransDocsDescr() {
        return this.transDocsDescr;
    }

    public void setTransDocsDescr(String transDocsDescr) {
        this.transDocsDescr = transDocsDescr;
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

    public BigDecimal getTransTypeId() {
        return transTypeId;
    }

    public void setTransTypeId(BigDecimal transTypeId) {
        this.transTypeId = transTypeId;
    }

    public BigDecimal getGlAccountId() {
        return glAccountId;
    }

    public void setGlAccountId(BigDecimal glAccountId) {
        this.glAccountId = glAccountId;
    }

    public BigDecimal getTaxRegimeId() {
        return taxRegimeId;
    }

    public void setTaxRegimeId(BigDecimal taxRegimeId) {
        this.taxRegimeId = taxRegimeId;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getLastupdatedate() {
        return lastupdatedate;
    }

    public void setLastupdatedate(Date lastupdatedate) {
        this.lastupdatedate = lastupdatedate;
    }

    public BigDecimal getDailyAmtLimitCr() {
        return dailyAmtLimitCr;
    }

    public void setDailyAmtLimitCr(BigDecimal dailyAmtLimitCr) {
        this.dailyAmtLimitCr = dailyAmtLimitCr;
    }

    public BigDecimal getDailyAmtLimitDr() {
        return dailyAmtLimitDr;
    }

    public void setDailyAmtLimitDr(BigDecimal dailyAmtLimitDr) {
        this.dailyAmtLimitDr = dailyAmtLimitDr;
    }

    public BigDecimal getMonthlyAmtLimitCr() {
        return monthlyAmtLimitCr;
    }

    public void setMonthlyAmtLimitCr(BigDecimal monthlyAmtLimitCr) {
        this.monthlyAmtLimitCr = monthlyAmtLimitCr;
    }

    public BigDecimal getMonthlyAmtLimitDr() {
        return monthlyAmtLimitDr;
    }

    public void setMonthlyAmtLimitDr(BigDecimal monthlyAmtLimitDr) {
        this.monthlyAmtLimitDr = monthlyAmtLimitDr;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
}