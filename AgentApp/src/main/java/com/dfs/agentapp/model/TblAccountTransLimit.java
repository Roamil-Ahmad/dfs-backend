package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_ACCOUNT_TRANS_LIMIT database table.
 * 
 */
@Entity
@Table(name="TBL_ACCOUNT_TRANS_LIMIT")
@NamedQuery(name="TblAccountTransLimit.findAll", query="SELECT t FROM TblAccountTransLimit t")
public class TblAccountTransLimit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_ACCOUNT_TRANS_LIMIT_ACCOUNTTRANSLIMITID_GENERATOR", sequenceName="TBL_ACCOUNT_TRANS_LIMIT_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_ACCOUNT_TRANS_LIMIT_ACCOUNTTRANSLIMITID_GENERATOR")
	@Column(name="ACCOUNT_TRANS_LIMIT_ID")
	private long accountTransLimitId;

	private Date asondate;

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

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MONTHLY_AMT_LIMIT_CR")
	private BigDecimal monthlyAmtLimitCr;

	@Column(name="MONTHLY_AMT_LIMIT_DR")
	private BigDecimal monthlyAmtLimitDr;

	@Column(name="MONTHLY_TRANS_LIMIT_CR")
	private BigDecimal monthlyTransLimitCr;

	@Column(name="MONTHLY_TRANS_LIMIT_DR")
	private BigDecimal monthlyTransLimitDr;

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
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TblAccount tblAccount;

	//bi-directional many-to-one association to TblTransDoc
	@ManyToOne
	@JoinColumn(name="TRANS_DOCS_ID")
	private TblTransDoc tblTransDoc;

	public TblAccountTransLimit() {
	}

	public long getAccountTransLimitId() {
		return this.accountTransLimitId;
	}

	public void setAccountTransLimitId(long accountTransLimitId) {
		this.accountTransLimitId = accountTransLimitId;
	}

	public Date getAsondate() {
		return this.asondate;
	}

	public void setAsondate(Date asondate) {
		this.asondate = asondate;
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

	public TblAccount getTblAccount() {
		return this.tblAccount;
	}

	public void setTblAccount(TblAccount tblAccount) {
		this.tblAccount = tblAccount;
	}

	public TblTransDoc getTblTransDoc() {
		return this.tblTransDoc;
	}

	public void setTblTransDoc(TblTransDoc tblTransDoc) {
		this.tblTransDoc = tblTransDoc;
	}

}