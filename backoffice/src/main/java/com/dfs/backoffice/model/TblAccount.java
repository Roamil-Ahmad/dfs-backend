package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the TBL_ACCOUNT database table.
 * 
 */
@Entity
@Table(name="TBL_ACCOUNT")
@NamedQuery(name="TblAccount.findAll", query="SELECT t FROM TblAccount t")
public class TblAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_ACCOUNT_ACCOUNTID_GENERATOR", sequenceName="TBL_ACCOUNT_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_ACCOUNT_ACCOUNTID_GENERATOR")
	@Column(name="ACCOUNT_ID")
	private long accountId;

	@Column(name="ACCOUNT_ALIAS")
	private String accountAlias;

	@Column(name="ACCOUNT_NO")
	private String accountNo;

	@Column(name="ACCOUNT_TITLE")
	private String accountTitle;

	@Column(name="AGENT_ACCOUNT")
	private String agentAccount;

	@Temporal(TemporalType.DATE)
	@Column(name="BALANCE_DATE")
	private Date balanceDate;

	@Column(name="BRANCH_ACCOUNT")
	private String branchAccount;

	// DB-managed: the column is NOT NULL with a DEFAULT ON NULL, so Oracle fills it on insert.
	// Hibernate must never write it -- an UPDATE would send the still-null in-memory value
	// and raise ORA-01407.
	@Column(name="CHARGES_PROFILE", insertable = false, updatable = false)
	private String chargesProfile;

	private String comments;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="CURRENT_BALANCE")
	private BigDecimal currentBalance;

	@Column(name="DAILY_AMT_LIMIT_CR")
	private BigDecimal dailyAmtLimitCr;

	@Column(name="DAILY_AMT_LIMIT_DR")
	private BigDecimal dailyAmtLimitDr;

	private String iban;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	@Column(name="MONTHLY_AMT_LIMIT_CR")
	private BigDecimal monthlyAmtLimitCr;

	@Column(name="MONTHLY_AMT_LIMIT_DR")
	private BigDecimal monthlyAmtLimitDr;

	@Column(name="QR_CODE")
	private String qrCode;

	private BigDecimal updateindex;

	@Column(name="YEARLY_AMT_LIMIT_CR")
	private BigDecimal yearlyAmtLimitCr;

	@Column(name="YEARLY_AMT_LIMIT_DR")
	private BigDecimal yearlyAmtLimitDr;

	//bi-directional many-to-one association to LkpAccountStatus
	@ManyToOne
	@JoinColumn(name="ACCOUNT_STATUS_ID")
	private LkpAccountStatus lkpAccountStatus;

	//bi-directional many-to-one association to LkpAccountType
	@ManyToOne
	@JoinColumn(name="ACCOUNT_TYPE_ID")
	private LkpAccountType lkpAccountType;

	//bi-directional many-to-one association to LkpRegistrationType
	@ManyToOne
	@JoinColumn(name="REGISTRATION_TYPE_ID")
	private LkpRegistrationType lkpRegistrationType;

	//bi-directional many-to-one association to TblAccountLevel
	@ManyToOne
	@JoinColumn(name="ACCOUNT_LEVEL_ID")
	private TblAccountLevel tblAccountLevel;

	//bi-directional many-to-one association to TblAgent
	@ManyToOne
	@JoinColumn(name="AGENT_ID")
	private TblAgent tblAgent;

	@Column(name="CUSTOMER_ID")
	private Long customerId;

	public long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getAccountAlias() {
		return this.accountAlias;
	}

	public void setAccountAlias(String accountAlias) {
		this.accountAlias = accountAlias;
	}

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountTitle() {
		return this.accountTitle;
	}

	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}

	public String getAgentAccount() {
		return this.agentAccount;
	}

	public void setAgentAccount(String agentAccount) {
		this.agentAccount = agentAccount;
	}

	public Date getBalanceDate() {
		return this.balanceDate;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	public String getBranchAccount() {
		return this.branchAccount;
	}

	public void setBranchAccount(String branchAccount) {
		this.branchAccount = branchAccount;
	}

	public String getChargesProfile() {
		return this.chargesProfile;
	}

	public void setChargesProfile(String chargesProfile) {
		this.chargesProfile = chargesProfile;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getCreatedate() {
		return createdate;
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

	public BigDecimal getCurrentBalance() {
		return this.currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public BigDecimal getDailyAmtLimitCr() {
		return this.dailyAmtLimitCr;
	}

	public void setDailyAmtLimitCr(BigDecimal dailyAmtLimitCr) {
		this.dailyAmtLimitCr = dailyAmtLimitCr;
	}

	public BigDecimal getDailyAmtLimitDr() {
		return this.dailyAmtLimitDr;
	}

	public void setDailyAmtLimitDr(BigDecimal dailyAmtLimitDr) {
		this.dailyAmtLimitDr = dailyAmtLimitDr;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getIsActive() {
		return this.isActive;
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
		return this.lastupdateuser;
	}

	public void setLastupdateuser(BigDecimal lastupdateuser) {
		this.lastupdateuser = lastupdateuser;
	}

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public BigDecimal getMonthlyAmtLimitCr() {
		return this.monthlyAmtLimitCr;
	}

	public void setMonthlyAmtLimitCr(BigDecimal monthlyAmtLimitCr) {
		this.monthlyAmtLimitCr = monthlyAmtLimitCr;
	}

	public BigDecimal getMonthlyAmtLimitDr() {
		return this.monthlyAmtLimitDr;
	}

	public void setMonthlyAmtLimitDr(BigDecimal monthlyAmtLimitDr) {
		this.monthlyAmtLimitDr = monthlyAmtLimitDr;
	}

	public String getQrCode() {
		return this.qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public BigDecimal getYearlyAmtLimitCr() {
		return this.yearlyAmtLimitCr;
	}

	public void setYearlyAmtLimitCr(BigDecimal yearlyAmtLimitCr) {
		this.yearlyAmtLimitCr = yearlyAmtLimitCr;
	}

	public BigDecimal getYearlyAmtLimitDr() {
		return this.yearlyAmtLimitDr;
	}

	public void setYearlyAmtLimitDr(BigDecimal yearlyAmtLimitDr) {
		this.yearlyAmtLimitDr = yearlyAmtLimitDr;
	}

	public LkpAccountStatus getLkpAccountStatus() {
		return this.lkpAccountStatus;
	}

	public void setLkpAccountStatus(LkpAccountStatus lkpAccountStatus) {
		this.lkpAccountStatus = lkpAccountStatus;
	}

	public LkpAccountType getLkpAccountType() {
		return this.lkpAccountType;
	}

	public void setLkpAccountType(LkpAccountType lkpAccountType) {
		this.lkpAccountType = lkpAccountType;
	}

	public LkpRegistrationType getLkpRegistrationType() {
		return this.lkpRegistrationType;
	}

	public void setLkpRegistrationType(LkpRegistrationType lkpRegistrationType) {
		this.lkpRegistrationType = lkpRegistrationType;
	}

	public TblAccountLevel getTblAccountLevel() {
		return this.tblAccountLevel;
	}

	public void setTblAccountLevel(TblAccountLevel tblAccountLevel) {
		this.tblAccountLevel = tblAccountLevel;
	}

	public TblAgent getTblAgent() {
		return tblAgent;
	}

	public void setTblAgent(TblAgent tblAgent) {
		this.tblAgent = tblAgent;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
}