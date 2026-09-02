package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


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
	@SequenceGenerator(name="TBL_TRANS_DOCS_TRANSDOCSID_GENERATOR", sequenceName="TBL_TRANS_DOCS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TRANS_DOCS_TRANSDOCSID_GENERATOR")
	@Column(name="TRANS_DOCS_ID")
	private long transDocsId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="LIMIT_YN")
	private String limitYn;

	@Column(name="TRANS_DOCS_CODE")
	private String transDocsCode;

	@Column(name="TRANS_DOCS_DESCR")
	private String transDocsDescr;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblAccountTransLimit
	@OneToMany(mappedBy="tblTransDoc")
	private List<TblAccountTransLimit> tblAccountTransLimits;

	//bi-directional many-to-one association to TblAppUserActivityLog
	@OneToMany(mappedBy="tblTransDoc")
	private List<TblAppUserActivityLog> tblAppUserActivityLogs;

	//bi-directional many-to-one association to TblCbsSummaryPostHead
	@OneToMany(mappedBy="tblTransDoc")
	private List<TblCbsSummaryPostHead> tblCbsSummaryPostHeads;

	//bi-directional many-to-one association to TblCbsTransHead
	@OneToMany(mappedBy="tblTransDoc")
	private List<TblCbsTransHead> tblCbsTransHeads;

	//bi-directional many-to-one association to TblCommissionDoc
	@OneToMany(mappedBy="tblTransDoc")
	private List<TblCommissionDoc> tblCommissionDocs;

	//bi-directional many-to-one association to TblSmsMessageTemplate
	@OneToMany(mappedBy="tblTransDoc")
	private List<TblSmsMessageTemplate> tblSmsMessageTemplates;

	//bi-directional many-to-one association to TblTransChargesDoc
	@OneToMany(mappedBy="tblTransDoc")
	private List<TblTransChargesDoc> tblTransChargesDocs;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to LkpTransType
	@ManyToOne
	@JoinColumn(name="TRANS_TYPE_ID")
	private LkpTransType lkpTransType;

	//bi-directional many-to-one association to TblGlAccount
	@ManyToOne
	@JoinColumn(name="GL_ACCOUNT_ID")
	private TblGlAccount tblGlAccount;

	//bi-directional many-to-one association to TblTaxRegime
	@ManyToOne
	@JoinColumn(name="TAX_REGIME_ID")
	private TblTaxRegime tblTaxRegime;

	//bi-directional many-to-one association to TblTransHead
	@OneToMany(mappedBy="tblTransDoc")
	private List<TblTransHead> tblTransHeads;

	//bi-directional many-to-one association to TblTransLimitDetail
	@OneToMany(mappedBy="tblTransDoc")
	private List<TblTransLimitDetail> tblTransLimitDetails;

	//bi-directional many-to-one association to TblUbpCompany
	@OneToMany(mappedBy="tblTransDoc")
	private List<TblUbpCompany> tblUbpCompanies;

	public TblTransDoc() {
	}

	public long getTransDocsId() {
		return this.transDocsId;
	}

	public void setTransDocsId(long transDocsId) {
		this.transDocsId = transDocsId;
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

	public List<TblAccountTransLimit> getTblAccountTransLimits() {
		return this.tblAccountTransLimits;
	}

	public void setTblAccountTransLimits(List<TblAccountTransLimit> tblAccountTransLimits) {
		this.tblAccountTransLimits = tblAccountTransLimits;
	}

	public TblAccountTransLimit addTblAccountTransLimit(TblAccountTransLimit tblAccountTransLimit) {
		getTblAccountTransLimits().add(tblAccountTransLimit);
		tblAccountTransLimit.setTblTransDoc(this);

		return tblAccountTransLimit;
	}

	public TblAccountTransLimit removeTblAccountTransLimit(TblAccountTransLimit tblAccountTransLimit) {
		getTblAccountTransLimits().remove(tblAccountTransLimit);
		tblAccountTransLimit.setTblTransDoc(null);

		return tblAccountTransLimit;
	}

	public List<TblAppUserActivityLog> getTblAppUserActivityLogs() {
		return this.tblAppUserActivityLogs;
	}

	public void setTblAppUserActivityLogs(List<TblAppUserActivityLog> tblAppUserActivityLogs) {
		this.tblAppUserActivityLogs = tblAppUserActivityLogs;
	}

	public TblAppUserActivityLog addTblAppUserActivityLog(TblAppUserActivityLog tblAppUserActivityLog) {
		getTblAppUserActivityLogs().add(tblAppUserActivityLog);
		tblAppUserActivityLog.setTblTransDoc(this);

		return tblAppUserActivityLog;
	}

	public TblAppUserActivityLog removeTblAppUserActivityLog(TblAppUserActivityLog tblAppUserActivityLog) {
		getTblAppUserActivityLogs().remove(tblAppUserActivityLog);
		tblAppUserActivityLog.setTblTransDoc(null);

		return tblAppUserActivityLog;
	}

	public List<TblCbsSummaryPostHead> getTblCbsSummaryPostHeads() {
		return this.tblCbsSummaryPostHeads;
	}

	public void setTblCbsSummaryPostHeads(List<TblCbsSummaryPostHead> tblCbsSummaryPostHeads) {
		this.tblCbsSummaryPostHeads = tblCbsSummaryPostHeads;
	}

	public TblCbsSummaryPostHead addTblCbsSummaryPostHead(TblCbsSummaryPostHead tblCbsSummaryPostHead) {
		getTblCbsSummaryPostHeads().add(tblCbsSummaryPostHead);
		tblCbsSummaryPostHead.setTblTransDoc(this);

		return tblCbsSummaryPostHead;
	}

	public TblCbsSummaryPostHead removeTblCbsSummaryPostHead(TblCbsSummaryPostHead tblCbsSummaryPostHead) {
		getTblCbsSummaryPostHeads().remove(tblCbsSummaryPostHead);
		tblCbsSummaryPostHead.setTblTransDoc(null);

		return tblCbsSummaryPostHead;
	}

	public List<TblCbsTransHead> getTblCbsTransHeads() {
		return this.tblCbsTransHeads;
	}

	public void setTblCbsTransHeads(List<TblCbsTransHead> tblCbsTransHeads) {
		this.tblCbsTransHeads = tblCbsTransHeads;
	}

	public TblCbsTransHead addTblCbsTransHead(TblCbsTransHead tblCbsTransHead) {
		getTblCbsTransHeads().add(tblCbsTransHead);
		tblCbsTransHead.setTblTransDoc(this);

		return tblCbsTransHead;
	}

	public TblCbsTransHead removeTblCbsTransHead(TblCbsTransHead tblCbsTransHead) {
		getTblCbsTransHeads().remove(tblCbsTransHead);
		tblCbsTransHead.setTblTransDoc(null);

		return tblCbsTransHead;
	}

	public List<TblCommissionDoc> getTblCommissionDocs() {
		return this.tblCommissionDocs;
	}

	public void setTblCommissionDocs(List<TblCommissionDoc> tblCommissionDocs) {
		this.tblCommissionDocs = tblCommissionDocs;
	}

	public TblCommissionDoc addTblCommissionDoc(TblCommissionDoc tblCommissionDoc) {
		getTblCommissionDocs().add(tblCommissionDoc);
		tblCommissionDoc.setTblTransDoc(this);

		return tblCommissionDoc;
	}

	public TblCommissionDoc removeTblCommissionDoc(TblCommissionDoc tblCommissionDoc) {
		getTblCommissionDocs().remove(tblCommissionDoc);
		tblCommissionDoc.setTblTransDoc(null);

		return tblCommissionDoc;
	}

	public List<TblSmsMessageTemplate> getTblSmsMessageTemplates() {
		return this.tblSmsMessageTemplates;
	}

	public void setTblSmsMessageTemplates(List<TblSmsMessageTemplate> tblSmsMessageTemplates) {
		this.tblSmsMessageTemplates = tblSmsMessageTemplates;
	}

	public TblSmsMessageTemplate addTblSmsMessageTemplate(TblSmsMessageTemplate tblSmsMessageTemplate) {
		getTblSmsMessageTemplates().add(tblSmsMessageTemplate);
		tblSmsMessageTemplate.setTblTransDoc(this);

		return tblSmsMessageTemplate;
	}

	public TblSmsMessageTemplate removeTblSmsMessageTemplate(TblSmsMessageTemplate tblSmsMessageTemplate) {
		getTblSmsMessageTemplates().remove(tblSmsMessageTemplate);
		tblSmsMessageTemplate.setTblTransDoc(null);

		return tblSmsMessageTemplate;
	}

	public List<TblTransChargesDoc> getTblTransChargesDocs() {
		return this.tblTransChargesDocs;
	}

	public void setTblTransChargesDocs(List<TblTransChargesDoc> tblTransChargesDocs) {
		this.tblTransChargesDocs = tblTransChargesDocs;
	}

	public TblTransChargesDoc addTblTransChargesDoc(TblTransChargesDoc tblTransChargesDoc) {
		getTblTransChargesDocs().add(tblTransChargesDoc);
		tblTransChargesDoc.setTblTransDoc(this);

		return tblTransChargesDoc;
	}

	public TblTransChargesDoc removeTblTransChargesDoc(TblTransChargesDoc tblTransChargesDoc) {
		getTblTransChargesDocs().remove(tblTransChargesDoc);
		tblTransChargesDoc.setTblTransDoc(null);

		return tblTransChargesDoc;
	}

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

	public LkpTransType getLkpTransType() {
		return this.lkpTransType;
	}

	public void setLkpTransType(LkpTransType lkpTransType) {
		this.lkpTransType = lkpTransType;
	}

	public TblGlAccount getTblGlAccount() {
		return this.tblGlAccount;
	}

	public void setTblGlAccount(TblGlAccount tblGlAccount) {
		this.tblGlAccount = tblGlAccount;
	}

	public TblTaxRegime getTblTaxRegime() {
		return this.tblTaxRegime;
	}

	public void setTblTaxRegime(TblTaxRegime tblTaxRegime) {
		this.tblTaxRegime = tblTaxRegime;
	}

	public List<TblTransHead> getTblTransHeads() {
		return this.tblTransHeads;
	}

	public void setTblTransHeads(List<TblTransHead> tblTransHeads) {
		this.tblTransHeads = tblTransHeads;
	}

	public TblTransHead addTblTransHead(TblTransHead tblTransHead) {
		getTblTransHeads().add(tblTransHead);
		tblTransHead.setTblTransDoc(this);

		return tblTransHead;
	}

	public TblTransHead removeTblTransHead(TblTransHead tblTransHead) {
		getTblTransHeads().remove(tblTransHead);
		tblTransHead.setTblTransDoc(null);

		return tblTransHead;
	}

	public List<TblTransLimitDetail> getTblTransLimitDetails() {
		return this.tblTransLimitDetails;
	}

	public void setTblTransLimitDetails(List<TblTransLimitDetail> tblTransLimitDetails) {
		this.tblTransLimitDetails = tblTransLimitDetails;
	}

	public TblTransLimitDetail addTblTransLimitDetail(TblTransLimitDetail tblTransLimitDetail) {
		getTblTransLimitDetails().add(tblTransLimitDetail);
		tblTransLimitDetail.setTblTransDoc(this);

		return tblTransLimitDetail;
	}

	public TblTransLimitDetail removeTblTransLimitDetail(TblTransLimitDetail tblTransLimitDetail) {
		getTblTransLimitDetails().remove(tblTransLimitDetail);
		tblTransLimitDetail.setTblTransDoc(null);

		return tblTransLimitDetail;
	}

	public List<TblUbpCompany> getTblUbpCompanies() {
		return this.tblUbpCompanies;
	}

	public void setTblUbpCompanies(List<TblUbpCompany> tblUbpCompanies) {
		this.tblUbpCompanies = tblUbpCompanies;
	}

	public TblUbpCompany addTblUbpCompany(TblUbpCompany tblUbpCompany) {
		getTblUbpCompanies().add(tblUbpCompany);
		tblUbpCompany.setTblTransDoc(this);

		return tblUbpCompany;
	}

	public TblUbpCompany removeTblUbpCompany(TblUbpCompany tblUbpCompany) {
		getTblUbpCompanies().remove(tblUbpCompany);
		tblUbpCompany.setTblTransDoc(null);

		return tblUbpCompany;
	}

}