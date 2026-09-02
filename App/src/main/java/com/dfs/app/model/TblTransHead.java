package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the TBL_TRANS_HEAD database table.
 * 
 */
@Entity
@Table(name="TBL_TRANS_HEAD")
@NamedQuery(name="TblTransHead.findAll", query="SELECT t FROM TblTransHead t")
public class TblTransHead implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TRANS_HEAD_TRANSHEADID_GENERATOR", sequenceName="TBL_TRANS_HEAD_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TRANS_HEAD_TRANSHEADID_GENERATOR")
	@Column(name="TRANS_HEAD_ID")
	private long transHeadId;

	@Column(name="CASHIN_TYPE")
	private String cashinType;

	private String comments;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DEPOSITOR_DOB")
	private Date depositorDob;

	@Column(name="DEPOSITOR_MOB")
	private String depositorMob;

	@Column(name="DEPOSITOR_NAME")
	private String depositorName;

	@Column(name="DEPOSITOR_NID")
	private String depositorNidNo;

	@Column(name="FEE_AMOUNT")
	private BigDecimal feeAmount;

	@Column(name="FEE_INCL_EXCL")
	private String feeInclExcl;

	@Column(name="FROM_ACCOUNT_ID")
	private BigDecimal fromAccountId;

	@Column(name="FROM_ACCOUNT_TITLE")
	private String fromAccountTitle;

	@Column(name="FROM_ACCOUNT_TYPE")
	private String fromAccountType;

	private String imei;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private double latitude;

	private double longitude;

	private String reason;

	@Column(name="\"RRN\"")
	private String rrn;

	private String stan;

	@Column(name="TO_ACCOUNT_ID")
	private BigDecimal toAccountId;

	@Column(name="TO_ACCOUNT_TITLE")
	private String toAccountTitle;

	@Column(name="TO_ACCOUNT_TYPE")
	private String toAccountType;

	@Column(name="TRANS_AMOUNT")
	private BigDecimal transAmount;

	@Column(name="TRANS_DATE")
	private Date transDate;

	@Column(name="TRANS_REFNUM")
	private BigDecimal transRefnum;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblDebitCardRequest
	@OneToMany(mappedBy="tblTransHead")
	private List<TblDebitCardRequest> tblDebitCardRequests;

	//bi-directional many-to-one association to TblOpclBalance
	@OneToMany(mappedBy="tblTransHead")
	private List<TblOpclBalance> tblOpclBalances;

	//bi-directional many-to-one association to TblTransDetail
	@OneToMany(mappedBy="tblTransHead")
	private List<TblTransDetail> tblTransDetails;

	//bi-directional many-to-one association to LkpBranch
	@ManyToOne
	@JoinColumn(name="BRANCH_ID")
	private LkpBranch lkpBranch;

	//bi-directional many-to-one association to LkpChannel
	@ManyToOne
	@JoinColumn(name="CHANNEL_ID")
	private LkpChannel lkpChannel;

	//bi-directional many-to-one association to TblMwRequest
	@ManyToOne
	@JoinColumn(name="MW_REQUEST_ID")
	private TblMwRequest tblMwRequest;

	//bi-directional many-to-one association to TblTransDoc
	@ManyToOne
	@JoinColumn(name="TRANS_DOCS_ID")
	private TblTransDoc tblTransDoc;

	//bi-directional many-to-one association to TblTransHead
	@ManyToOne
	@JoinColumn(name="OTC_SR_TRANS_HEAD_ID")
	private TblTransHead tblTransHead1;

	//bi-directional many-to-one association to TblTransHead
	@OneToMany(mappedBy="tblTransHead1")
	private List<TblTransHead> tblTransHeads1;

	//bi-directional many-to-one association to TblTransHead
	@ManyToOne
	@JoinColumn(name="PAR_TRANS_HEAD_ID")
	private TblTransHead tblTransHead2;

	//bi-directional many-to-one association to TblTransHead
	@OneToMany(mappedBy="tblTransHead2")
	private List<TblTransHead> tblTransHeads2;

	public TblTransHead() {
	}

	public long getTransHeadId() {
		return this.transHeadId;
	}

	public void setTransHeadId(long transHeadId) {
		this.transHeadId = transHeadId;
	}

	public String getCashinType() {
		return this.cashinType;
	}

	public void setCashinType(String cashinType) {
		this.cashinType = cashinType;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public Date getDepositorDob() {
		return this.depositorDob;
	}

	public void setDepositorDob(Date depositorDob) {
		this.depositorDob = depositorDob;
	}

	public String getDepositorMob() {
		return this.depositorMob;
	}

	public void setDepositorMob(String depositorMob) {
		this.depositorMob = depositorMob;
	}

	public String getDepositorName() {
		return this.depositorName;
	}

	public void setDepositorName(String depositorName) {
		this.depositorName = depositorName;
	}

	public String getDepositorNidNo() {
		return this.depositorNidNo;
	}

	public void setDepositorNidNo(String depositorNidNo) {
		this.depositorNidNo = depositorNidNo;
	}

	public BigDecimal getFeeAmount() {
		return this.feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getFeeInclExcl() {
		return this.feeInclExcl;
	}

	public void setFeeInclExcl(String feeInclExcl) {
		this.feeInclExcl = feeInclExcl;
	}

	public BigDecimal getFromAccountId() {
		return this.fromAccountId;
	}

	public void setFromAccountId(BigDecimal fromAccountId) {
		this.fromAccountId = fromAccountId;
	}

	public String getFromAccountTitle() {
		return this.fromAccountTitle;
	}

	public void setFromAccountTitle(String fromAccountTitle) {
		this.fromAccountTitle = fromAccountTitle;
	}

	public String getFromAccountType() {
		return this.fromAccountType;
	}

	public void setFromAccountType(String fromAccountType) {
		this.fromAccountType = fromAccountType;
	}

	public String getImei() {
		return this.imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
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

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRrn() {
		return this.rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getStan() {
		return this.stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	public BigDecimal getToAccountId() {
		return this.toAccountId;
	}

	public void setToAccountId(BigDecimal toAccountId) {
		this.toAccountId = toAccountId;
	}

	public String getToAccountTitle() {
		return this.toAccountTitle;
	}

	public void setToAccountTitle(String toAccountTitle) {
		this.toAccountTitle = toAccountTitle;
	}

	public String getToAccountType() {
		return this.toAccountType;
	}

	public void setToAccountType(String toAccountType) {
		this.toAccountType = toAccountType;
	}

	public BigDecimal getTransAmount() {
		return this.transAmount;
	}

	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}

	public Object getTransDate() {
		return this.transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public BigDecimal getTransRefnum() {
		return this.transRefnum;
	}

	public void setTransRefnum(BigDecimal transRefnum) {
		this.transRefnum = transRefnum;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public List<TblDebitCardRequest> getTblDebitCardRequests() {
		return this.tblDebitCardRequests;
	}

	public void setTblDebitCardRequests(List<TblDebitCardRequest> tblDebitCardRequests) {
		this.tblDebitCardRequests = tblDebitCardRequests;
	}

	public TblDebitCardRequest addTblDebitCardRequest(TblDebitCardRequest tblDebitCardRequest) {
		getTblDebitCardRequests().add(tblDebitCardRequest);
		tblDebitCardRequest.setTblTransHead(this);

		return tblDebitCardRequest;
	}

	public TblDebitCardRequest removeTblDebitCardRequest(TblDebitCardRequest tblDebitCardRequest) {
		getTblDebitCardRequests().remove(tblDebitCardRequest);
		tblDebitCardRequest.setTblTransHead(null);

		return tblDebitCardRequest;
	}

	public List<TblOpclBalance> getTblOpclBalances() {
		return this.tblOpclBalances;
	}

	public void setTblOpclBalances(List<TblOpclBalance> tblOpclBalances) {
		this.tblOpclBalances = tblOpclBalances;
	}

	public TblOpclBalance addTblOpclBalance(TblOpclBalance tblOpclBalance) {
		getTblOpclBalances().add(tblOpclBalance);
		tblOpclBalance.setTblTransHead(this);

		return tblOpclBalance;
	}

	public TblOpclBalance removeTblOpclBalance(TblOpclBalance tblOpclBalance) {
		getTblOpclBalances().remove(tblOpclBalance);
		tblOpclBalance.setTblTransHead(null);

		return tblOpclBalance;
	}

	public List<TblTransDetail> getTblTransDetails() {
		return this.tblTransDetails;
	}

	public void setTblTransDetails(List<TblTransDetail> tblTransDetails) {
		this.tblTransDetails = tblTransDetails;
	}

	public TblTransDetail addTblTransDetail(TblTransDetail tblTransDetail) {
		getTblTransDetails().add(tblTransDetail);
		tblTransDetail.setTblTransHead(this);

		return tblTransDetail;
	}

	public TblTransDetail removeTblTransDetail(TblTransDetail tblTransDetail) {
		getTblTransDetails().remove(tblTransDetail);
		tblTransDetail.setTblTransHead(null);

		return tblTransDetail;
	}

	public LkpBranch getLkpBranch() {
		return this.lkpBranch;
	}

	public void setLkpBranch(LkpBranch lkpBranch) {
		this.lkpBranch = lkpBranch;
	}

	public LkpChannel getLkpChannel() {
		return this.lkpChannel;
	}

	public void setLkpChannel(LkpChannel lkpChannel) {
		this.lkpChannel = lkpChannel;
	}

	public TblMwRequest getTblMwRequest() {
		return this.tblMwRequest;
	}

	public void setTblMwRequest(TblMwRequest tblMwRequest) {
		this.tblMwRequest = tblMwRequest;
	}

	public TblTransDoc getTblTransDoc() {
		return this.tblTransDoc;
	}

	public void setTblTransDoc(TblTransDoc tblTransDoc) {
		this.tblTransDoc = tblTransDoc;
	}

	public TblTransHead getTblTransHead1() {
		return this.tblTransHead1;
	}

	public void setTblTransHead1(TblTransHead tblTransHead1) {
		this.tblTransHead1 = tblTransHead1;
	}

	public List<TblTransHead> getTblTransHeads1() {
		return this.tblTransHeads1;
	}

	public void setTblTransHeads1(List<TblTransHead> tblTransHeads1) {
		this.tblTransHeads1 = tblTransHeads1;
	}

	public TblTransHead addTblTransHeads1(TblTransHead tblTransHeads1) {
		getTblTransHeads1().add(tblTransHeads1);
		tblTransHeads1.setTblTransHead1(this);

		return tblTransHeads1;
	}

	public TblTransHead removeTblTransHeads1(TblTransHead tblTransHeads1) {
		getTblTransHeads1().remove(tblTransHeads1);
		tblTransHeads1.setTblTransHead1(null);

		return tblTransHeads1;
	}

	public TblTransHead getTblTransHead2() {
		return this.tblTransHead2;
	}

	public void setTblTransHead2(TblTransHead tblTransHead2) {
		this.tblTransHead2 = tblTransHead2;
	}

	public List<TblTransHead> getTblTransHeads2() {
		return this.tblTransHeads2;
	}

	public void setTblTransHeads2(List<TblTransHead> tblTransHeads2) {
		this.tblTransHeads2 = tblTransHeads2;
	}

	public TblTransHead addTblTransHeads2(TblTransHead tblTransHeads2) {
		getTblTransHeads2().add(tblTransHeads2);
		tblTransHeads2.setTblTransHead2(this);

		return tblTransHeads2;
	}

	public TblTransHead removeTblTransHeads2(TblTransHead tblTransHeads2) {
		getTblTransHeads2().remove(tblTransHeads2);
		tblTransHeads2.setTblTransHead2(null);

		return tblTransHeads2;
	}

}