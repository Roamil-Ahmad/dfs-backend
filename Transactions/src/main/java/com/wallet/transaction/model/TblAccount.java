package com.wallet.transaction.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="CURRENT_BALANCE")
	private BigDecimal currentBalance;

	@Column(name="DAILY_AMT_LIMIT_CR")
	private BigDecimal dailyAmtLimitCr;

	@Column(name="DAILY_AMT_LIMIT_DR")
	private BigDecimal dailyAmtLimitDr;

	@Temporal(TemporalType.DATE)
	@Column(name="DORMANT_DATE")
	private Date dormantDate;

	@Temporal(TemporalType.DATE)
	@Column(name="DORMANT_REMOVAL_DATE")
	private Date dormantRemovalDate;

	private String iban;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

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

	//bi-directional many-to-one association to TblCustomer
	@ManyToOne
	@JoinColumn(name="CUSTOMER_ID")
	private TblCustomer tblCustomer;

	//bi-directional many-to-one association to TblAccountDebitCard
	@OneToMany(mappedBy="tblAccount")
	@JsonIgnore
	private List<TblAccountDebitCard> tblAccountDebitCards;

	//bi-directional many-to-one association to TblAccountLimit
	@OneToMany(mappedBy="tblAccount")
	@JsonIgnore
	private List<TblAccountLimit> tblAccountLimits;

	//bi-directional many-to-one association to TblAccountTransLimit
	@OneToMany(mappedBy="tblAccount")
	@JsonIgnore
	private List<TblAccountTransLimit> tblAccountTransLimits;

	//bi-directional many-to-one association to TblDebitCardRequest
	@OneToMany(mappedBy="tblAccount")
	@JsonIgnore
	private List<TblDebitCardRequest> tblDebitCardRequests;

	//bi-directional many-to-one association to TblRequestMoney
	@OneToMany(mappedBy="tblAccount1")
	@JsonIgnore
	private List<TblRequestMoney> tblRequestMoneys1;

	//bi-directional many-to-one association to TblRequestMoney
	@OneToMany(mappedBy="tblAccount2")
	@JsonIgnore
	private List<TblRequestMoney> tblRequestMoneys2;

	public TblAccount() {
	}

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

	public Date getDormantDate() {
		return this.dormantDate;
	}

	public void setDormantDate(Date dormantDate) {
		this.dormantDate = dormantDate;
	}

	public Date getDormantRemovalDate() {
		return this.dormantRemovalDate;
	}

	public void setDormantRemovalDate(Date dormantRemovalDate) {
		this.dormantRemovalDate = dormantRemovalDate;
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
		return this.tblAgent;
	}

	public void setTblAgent(TblAgent tblAgent) {
		this.tblAgent = tblAgent;
	}

	public TblCustomer getTblCustomer() {
		return this.tblCustomer;
	}

	public void setTblCustomer(TblCustomer tblCustomer) {
		this.tblCustomer = tblCustomer;
	}

	public List<TblAccountDebitCard> getTblAccountDebitCards() {
		return this.tblAccountDebitCards;
	}

	public void setTblAccountDebitCards(List<TblAccountDebitCard> tblAccountDebitCards) {
		this.tblAccountDebitCards = tblAccountDebitCards;
	}

	public TblAccountDebitCard addTblAccountDebitCard(TblAccountDebitCard tblAccountDebitCard) {
		getTblAccountDebitCards().add(tblAccountDebitCard);
		tblAccountDebitCard.setTblAccount(this);

		return tblAccountDebitCard;
	}

	public TblAccountDebitCard removeTblAccountDebitCard(TblAccountDebitCard tblAccountDebitCard) {
		getTblAccountDebitCards().remove(tblAccountDebitCard);
		tblAccountDebitCard.setTblAccount(null);

		return tblAccountDebitCard;
	}

	public List<TblAccountLimit> getTblAccountLimits() {
		return this.tblAccountLimits;
	}

	public void setTblAccountLimits(List<TblAccountLimit> tblAccountLimits) {
		this.tblAccountLimits = tblAccountLimits;
	}

	public TblAccountLimit addTblAccountLimit(TblAccountLimit tblAccountLimit) {
		getTblAccountLimits().add(tblAccountLimit);
		tblAccountLimit.setTblAccount(this);

		return tblAccountLimit;
	}

	public TblAccountLimit removeTblAccountLimit(TblAccountLimit tblAccountLimit) {
		getTblAccountLimits().remove(tblAccountLimit);
		tblAccountLimit.setTblAccount(null);

		return tblAccountLimit;
	}

	public List<TblAccountTransLimit> getTblAccountTransLimits() {
		return this.tblAccountTransLimits;
	}

	public void setTblAccountTransLimits(List<TblAccountTransLimit> tblAccountTransLimits) {
		this.tblAccountTransLimits = tblAccountTransLimits;
	}

	public TblAccountTransLimit addTblAccountTransLimit(TblAccountTransLimit tblAccountTransLimit) {
		getTblAccountTransLimits().add(tblAccountTransLimit);
		tblAccountTransLimit.setTblAccount(this);

		return tblAccountTransLimit;
	}

	public TblAccountTransLimit removeTblAccountTransLimit(TblAccountTransLimit tblAccountTransLimit) {
		getTblAccountTransLimits().remove(tblAccountTransLimit);
		tblAccountTransLimit.setTblAccount(null);

		return tblAccountTransLimit;
	}

	public List<TblDebitCardRequest> getTblDebitCardRequests() {
		return this.tblDebitCardRequests;
	}

	public void setTblDebitCardRequests(List<TblDebitCardRequest> tblDebitCardRequests) {
		this.tblDebitCardRequests = tblDebitCardRequests;
	}

	public TblDebitCardRequest addTblDebitCardRequest(TblDebitCardRequest tblDebitCardRequest) {
		getTblDebitCardRequests().add(tblDebitCardRequest);
		tblDebitCardRequest.setTblAccount(this);

		return tblDebitCardRequest;
	}

	public TblDebitCardRequest removeTblDebitCardRequest(TblDebitCardRequest tblDebitCardRequest) {
		getTblDebitCardRequests().remove(tblDebitCardRequest);
		tblDebitCardRequest.setTblAccount(null);

		return tblDebitCardRequest;
	}

	public List<TblRequestMoney> getTblRequestMoneys1() {
		return this.tblRequestMoneys1;
	}

	public void setTblRequestMoneys1(List<TblRequestMoney> tblRequestMoneys1) {
		this.tblRequestMoneys1 = tblRequestMoneys1;
	}

	public TblRequestMoney addTblRequestMoneys1(TblRequestMoney tblRequestMoneys1) {
		getTblRequestMoneys1().add(tblRequestMoneys1);
		tblRequestMoneys1.setTblAccount1(this);

		return tblRequestMoneys1;
	}

	public TblRequestMoney removeTblRequestMoneys1(TblRequestMoney tblRequestMoneys1) {
		getTblRequestMoneys1().remove(tblRequestMoneys1);
		tblRequestMoneys1.setTblAccount1(null);

		return tblRequestMoneys1;
	}

	public List<TblRequestMoney> getTblRequestMoneys2() {
		return this.tblRequestMoneys2;
	}

	public void setTblRequestMoneys2(List<TblRequestMoney> tblRequestMoneys2) {
		this.tblRequestMoneys2 = tblRequestMoneys2;
	}

	public TblRequestMoney addTblRequestMoneys2(TblRequestMoney tblRequestMoneys2) {
		getTblRequestMoneys2().add(tblRequestMoneys2);
		tblRequestMoneys2.setTblAccount2(this);

		return tblRequestMoneys2;
	}

	public TblRequestMoney removeTblRequestMoneys2(TblRequestMoney tblRequestMoneys2) {
		getTblRequestMoneys2().remove(tblRequestMoneys2);
		tblRequestMoneys2.setTblAccount2(null);

		return tblRequestMoneys2;
	}

}