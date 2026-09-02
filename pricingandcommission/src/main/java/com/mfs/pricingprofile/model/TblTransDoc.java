package com.mfs.pricingprofile.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_TRANS_DOCS database table.
 * 
 */
@Entity
@Table(name="TBL_TRANS_DOCS")
@NamedQuery(name="TblTransDoc.findAll", query="SELECT t FROM TblTransDoc t")
public class TblTransDoc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TRANS_DOCS_TRANSDOCSID_GENERATOR", sequenceName="TBL_TRANS_DOCS_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TRANS_DOCS_TRANSDOCSID_GENERATOR")
	@Column(name="TRANS_DOCS_ID")
	private long transDocsId;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="LIMIT_YN")
	private String limitYn;

	@Column(name="TAX_REGIME_ID")
	private BigDecimal taxRegimeId;

	@Column(name="TRANS_DOCS_CODE")
	private String transDocsCode;

	@Column(name="TRANS_DOCS_DESCR")
	private String transDocsDescr;

	@Column(name="TRANS_TYPE_ID")
	private BigDecimal transTypeId;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblGlAccount
	@ManyToOne
	@JoinColumn(name="GL_ACCOUNT_ID")
	private TblGlAccount tblGlAccount;

	public long getTransDocsId() {
		return this.transDocsId;
	}

	public void setTransDocsId(long transDocsId) {
		this.transDocsId = transDocsId;
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

	public String getLimitYn() {
		return this.limitYn;
	}

	public void setLimitYn(String limitYn) {
		this.limitYn = limitYn;
	}

	public BigDecimal getTaxRegimeId() {
		return this.taxRegimeId;
	}

	public void setTaxRegimeId(BigDecimal taxRegimeId) {
		this.taxRegimeId = taxRegimeId;
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

	public BigDecimal getTransTypeId() {
		return this.transTypeId;
	}

	public void setTransTypeId(BigDecimal transTypeId) {
		this.transTypeId = transTypeId;
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

	public TblGlAccount getTblGlAccount() {
		return this.tblGlAccount;
	}

	public void setTblGlAccount(TblGlAccount tblGlAccount) {
		this.tblGlAccount = tblGlAccount;
	}

}