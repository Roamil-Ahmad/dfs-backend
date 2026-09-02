package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the TBL_TRANS_LIMIT database table.
 * 
 */
@Entity
@Table(name="TBL_TRANS_LIMIT")
@NamedQuery(name="TblTransLimit.findAll", query="SELECT t FROM TblTransLimit t")
public class TblTransLimit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TRANS_LIMIT_TRANSLIMITID_GENERATOR", sequenceName="TBL_TRANS_LIMIT_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TRANS_LIMIT_TRANSLIMITID_GENERATOR")
	@Column(name="TRANS_LIMIT_ID")
	private long transLimitId;

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

	@Column(name="EXCLUDE_LIMIT")
	private String excludeLimit;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="LIMIT_PROFILE_NAME")
	private String limitProfileName;

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

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblTransLimitDetail
	@OneToMany(mappedBy="tblTransLimit")
	private List<TblTransLimitDetail> tblTransLimitDetails;

	public TblTransLimit() {
	}

	public long getTransLimitId() {
		return this.transLimitId;
	}

	public void setTransLimitId(long transLimitId) {
		this.transLimitId = transLimitId;
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

	public String getExcludeLimit() {
		return this.excludeLimit;
	}

	public void setExcludeLimit(String excludeLimit) {
		this.excludeLimit = excludeLimit;
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

	public String getLimitProfileName() {
		return this.limitProfileName;
	}

	public void setLimitProfileName(String limitProfileName) {
		this.limitProfileName = limitProfileName;
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

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

	public List<TblTransLimitDetail> getTblTransLimitDetails() {
		return this.tblTransLimitDetails;
	}

	public void setTblTransLimitDetails(List<TblTransLimitDetail> tblTransLimitDetails) {
		this.tblTransLimitDetails = tblTransLimitDetails;
	}

	public TblTransLimitDetail addTblTransLimitDetail(TblTransLimitDetail tblTransLimitDetail) {
		getTblTransLimitDetails().add(tblTransLimitDetail);
		tblTransLimitDetail.setTblTransLimit(this);

		return tblTransLimitDetail;
	}

	public TblTransLimitDetail removeTblTransLimitDetail(TblTransLimitDetail tblTransLimitDetail) {
		getTblTransLimitDetails().remove(tblTransLimitDetail);
		tblTransLimitDetail.setTblTransLimit(null);

		return tblTransLimitDetail;
	}

}