package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_TAX_REGIME database table.
 * 
 */
@Entity
@Table(name="TBL_TAX_REGIME")
@NamedQuery(name="TblTaxRegime.findAll", query="SELECT t FROM TblTaxRegime t")
public class TblTaxRegime implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TAX_REGIME_TAXREGIMEID_GENERATOR", sequenceName="TBL_TAX_REGIME_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TAX_REGIME_TAXREGIMEID_GENERATOR")
	@Column(name="TAX_REGIME_ID")
	private long taxRegimeId;

	@Column(name="APPLICABLE_ON")
	private String applicableOn;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="EFFECTIVE_FROM")
	private Date effectiveFrom;

	@Column(name="EFFECTIVE_TO")
	private Date effectiveTo;

	@Column(name="IS_ACTIVE")
	private String isActive;


	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="TAX_REGIME_CODE")
	private String taxRegimeCode;

	@Column(name="TAX_REGIME_DESCR")
	private String taxRegimeDescr;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblTaxDetail
	@OneToMany(mappedBy="tblTaxRegime")
	private List<TblTaxDetail> tblTaxDetails;

	//bi-directional many-to-one association to TblTaxRate
	@OneToMany(mappedBy="tblTaxRegime")
	private List<TblTaxRate> tblTaxRates;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to LkpTaxType
	@ManyToOne
	@JoinColumn(name="TAX_TYPE_ID")
	private LkpTaxType lkpTaxType;

	//bi-directional many-to-one association to TblGlAccount
	@ManyToOne
	@JoinColumn(name="GL_ACCOUNT_ID")
	private TblGlAccount tblGlAccount;

	//bi-directional many-to-one association to TblTransDoc
	@OneToMany(mappedBy="tblTaxRegime")
	private List<TblTransDoc> tblTransDocs;

	public TblTaxRegime() {
	}

	public long getTaxRegimeId() {
		return this.taxRegimeId;
	}

	public void setTaxRegimeId(long taxRegimeId) {
		this.taxRegimeId = taxRegimeId;
	}

	public String getApplicableOn() {
		return this.applicableOn;
	}

	public void setApplicableOn(String applicableOn) {
		this.applicableOn = applicableOn;
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

	public Date getEffectiveFrom() {
		return this.effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Date getEffectiveTo() {
		return this.effectiveTo;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}


	public Object getLastupdatedate() {
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

	public String getTaxRegimeCode() {
		return this.taxRegimeCode;
	}

	public void setTaxRegimeCode(String taxRegimeCode) {
		this.taxRegimeCode = taxRegimeCode;
	}

	public String getTaxRegimeDescr() {
		return this.taxRegimeDescr;
	}

	public void setTaxRegimeDescr(String taxRegimeDescr) {
		this.taxRegimeDescr = taxRegimeDescr;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public List<TblTaxDetail> getTblTaxDetails() {
		return this.tblTaxDetails;
	}

	public void setTblTaxDetails(List<TblTaxDetail> tblTaxDetails) {
		this.tblTaxDetails = tblTaxDetails;
	}

	public TblTaxDetail addTblTaxDetail(TblTaxDetail tblTaxDetail) {
		getTblTaxDetails().add(tblTaxDetail);
		tblTaxDetail.setTblTaxRegime(this);

		return tblTaxDetail;
	}

	public TblTaxDetail removeTblTaxDetail(TblTaxDetail tblTaxDetail) {
		getTblTaxDetails().remove(tblTaxDetail);
		tblTaxDetail.setTblTaxRegime(null);

		return tblTaxDetail;
	}

	public List<TblTaxRate> getTblTaxRates() {
		return this.tblTaxRates;
	}

	public void setTblTaxRates(List<TblTaxRate> tblTaxRates) {
		this.tblTaxRates = tblTaxRates;
	}

	public TblTaxRate addTblTaxRate(TblTaxRate tblTaxRate) {
		getTblTaxRates().add(tblTaxRate);
		tblTaxRate.setTblTaxRegime(this);

		return tblTaxRate;
	}

	public TblTaxRate removeTblTaxRate(TblTaxRate tblTaxRate) {
		getTblTaxRates().remove(tblTaxRate);
		tblTaxRate.setTblTaxRegime(null);

		return tblTaxRate;
	}

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

	public LkpTaxType getLkpTaxType() {
		return this.lkpTaxType;
	}

	public void setLkpTaxType(LkpTaxType lkpTaxType) {
		this.lkpTaxType = lkpTaxType;
	}

	public TblGlAccount getTblGlAccount() {
		return this.tblGlAccount;
	}

	public void setTblGlAccount(TblGlAccount tblGlAccount) {
		this.tblGlAccount = tblGlAccount;
	}

	public List<TblTransDoc> getTblTransDocs() {
		return this.tblTransDocs;
	}

	public void setTblTransDocs(List<TblTransDoc> tblTransDocs) {
		this.tblTransDocs = tblTransDocs;
	}

	public TblTransDoc addTblTransDoc(TblTransDoc tblTransDoc) {
		getTblTransDocs().add(tblTransDoc);
		tblTransDoc.setTblTaxRegime(this);

		return tblTransDoc;
	}

	public TblTransDoc removeTblTransDoc(TblTransDoc tblTransDoc) {
		getTblTransDocs().remove(tblTransDoc);
		tblTransDoc.setTblTaxRegime(null);

		return tblTransDoc;
	}

}