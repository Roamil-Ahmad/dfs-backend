package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_ACCOUNT_LEVEL database table.
 * 
 */
@Entity
@Table(name="TBL_ACCOUNT_LEVEL")
@NamedQuery(name="TblAccountLevel.findAll", query="SELECT t FROM TblAccountLevel t")
public class TblAccountLevel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_ACCOUNT_LEVEL_ACCOUNTLEVELID_GENERATOR", sequenceName="TBL_ACCOUNT_LEVEL_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_ACCOUNT_LEVEL_ACCOUNTLEVELID_GENERATOR")
	@Column(name="ACCOUNT_LEVEL_ID")
	private long accountLevelId;

	@Column(name="ACCOUNT_LEVEL_CODE")
	private String accountLevelCode;

	@Column(name="ACCOUNT_LEVEL_DESCR")
	private String accountLevelDescr;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DAILY_AMT_LIMIT_CR")
	private BigDecimal dailyAmtLimitCr;

	@Column(name="DAILY_AMT_LIMIT_DR")
	private BigDecimal dailyAmtLimitDr;

	@Column(name="DAILY_TRANS_LIMIT_CR")
	private BigDecimal dailyTransLimitCr;

	@Column(name="DAILY_TRANS_LIMIT_DR")
	private BigDecimal dailyTransLimitDr;

	private String dflt;

	@Column(name="GL_ACCOUNT_ID")
	private BigDecimal glAccountId;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MAX_AMT_LIMIT")
	private BigDecimal maxAmtLimit;

	@Column(name="MAX_AMT_PER_TXN")
	private BigDecimal maxAmtPerTxn;

	@Column(name="MONTHLY_AMT_LIMIT_CR")
	private BigDecimal monthlyAmtLimitCr;

	@Column(name="MONTHLY_AMT_LIMIT_DR")
	private BigDecimal monthlyAmtLimitDr;

	@Column(name="MONTHLY_TRANS_LIMIT_CR")
	private BigDecimal monthlyTransLimitCr;

	@Column(name="MONTHLY_TRANS_LIMIT_DR")
	private BigDecimal monthlyTransLimitDr;

	@Column(name="STATUS_ID")
	private BigDecimal statusId;

	private BigDecimal updateindex;

	@Column(name="YEARLY_AMT_LIMIT_CR")
	private BigDecimal yearlyAmtLimitCr;

	@Column(name="YEARLY_AMT_LIMIT_DR")
	private BigDecimal yearlyAmtLimitDr;

	@Column(name="YEARLY_TRANS_LIMIT_CR")
	private BigDecimal yearlyTransLimitCr;

	@Column(name="YEARLY_TRANS_LIMIT_DR")
	private BigDecimal yearlyTransLimitDr;

	//bi-directional many-to-one association to TblAccount
	@OneToMany(mappedBy="tblAccountLevel")
	private List<TblAccount> tblAccounts;

	public TblAccountLevel() {
	}

	public long getAccountLevelId() {
		return this.accountLevelId;
	}

	public void setAccountLevelId(long accountLevelId) {
		this.accountLevelId = accountLevelId;
	}

	public String getAccountLevelCode() {
		return this.accountLevelCode;
	}

	public void setAccountLevelCode(String accountLevelCode) {
		this.accountLevelCode = accountLevelCode;
	}

	public String getAccountLevelDescr() {
		return this.accountLevelDescr;
	}

	public void setAccountLevelDescr(String accountLevelDescr) {
		this.accountLevelDescr = accountLevelDescr;
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

	public BigDecimal getDailyTransLimitCr() {
		return this.dailyTransLimitCr;
	}

	public void setDailyTransLimitCr(BigDecimal dailyTransLimitCr) {
		this.dailyTransLimitCr = dailyTransLimitCr;
	}

	public BigDecimal getDailyTransLimitDr() {
		return this.dailyTransLimitDr;
	}

	public void setDailyTransLimitDr(BigDecimal dailyTransLimitDr) {
		this.dailyTransLimitDr = dailyTransLimitDr;
	}

	public String getDflt() {
		return this.dflt;
	}

	public void setDflt(String dflt) {
		this.dflt = dflt;
	}

	public BigDecimal getGlAccountId() {
		return this.glAccountId;
	}

	public void setGlAccountId(BigDecimal glAccountId) {
		this.glAccountId = glAccountId;
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

	public BigDecimal getMaxAmtLimit() {
		return this.maxAmtLimit;
	}

	public void setMaxAmtLimit(BigDecimal maxAmtLimit) {
		this.maxAmtLimit = maxAmtLimit;
	}

	public BigDecimal getMaxAmtPerTxn() {
		return this.maxAmtPerTxn;
	}

	public void setMaxAmtPerTxn(BigDecimal maxAmtPerTxn) {
		this.maxAmtPerTxn = maxAmtPerTxn;
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

	public BigDecimal getMonthlyTransLimitCr() {
		return this.monthlyTransLimitCr;
	}

	public void setMonthlyTransLimitCr(BigDecimal monthlyTransLimitCr) {
		this.monthlyTransLimitCr = monthlyTransLimitCr;
	}

	public BigDecimal getMonthlyTransLimitDr() {
		return this.monthlyTransLimitDr;
	}

	public void setMonthlyTransLimitDr(BigDecimal monthlyTransLimitDr) {
		this.monthlyTransLimitDr = monthlyTransLimitDr;
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

	public BigDecimal getYearlyTransLimitCr() {
		return this.yearlyTransLimitCr;
	}

	public void setYearlyTransLimitCr(BigDecimal yearlyTransLimitCr) {
		this.yearlyTransLimitCr = yearlyTransLimitCr;
	}

	public BigDecimal getYearlyTransLimitDr() {
		return this.yearlyTransLimitDr;
	}

	public void setYearlyTransLimitDr(BigDecimal yearlyTransLimitDr) {
		this.yearlyTransLimitDr = yearlyTransLimitDr;
	}

	public List<TblAccount> getTblAccounts() {
		return this.tblAccounts;
	}

	public void setTblAccounts(List<TblAccount> tblAccounts) {
		this.tblAccounts = tblAccounts;
	}

	public TblAccount addTblAccount(TblAccount tblAccount) {
		getTblAccounts().add(tblAccount);
		tblAccount.setTblAccountLevel(this);

		return tblAccount;
	}

	public TblAccount removeTblAccount(TblAccount tblAccount) {
		getTblAccounts().remove(tblAccount);
		tblAccount.setTblAccountLevel(null);

		return tblAccount;
	}

}