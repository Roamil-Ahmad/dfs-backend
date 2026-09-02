package com.dfs.backoffice.model;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_PROVINCE_LIMIT database table.
 * 
 */
@Entity
@Data
@Table(name="TBL_PROVINCE_LIMIT")
@NamedQuery(name="TblProvinceLimit.findAll", query="SELECT t FROM TblProvinceLimit t")
public class TblProvinceLimit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_PROVINCE_LIMIT_PROVINCELIMITID_GENERATOR", sequenceName="TBL_PROVINCE_LIMIT_SEQ",  allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_PROVINCE_LIMIT_PROVINCELIMITID_GENERATOR")
	@Column(name="PROVINCE_LIMIT_ID")
	private long provinceLimitId;

	@Column(name="ACCOUNT_LEVEL_ID")
	private BigDecimal accountLevelId;

	@Temporal(TemporalType.DATE)
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

	@Temporal(TemporalType.DATE)
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

	@Column(name="RISK_PROFILE")
	private String riskProfile;

	private BigDecimal updateindex;

	@Column(name="YEARLY_AMT_LIMIT_CR")
	private BigDecimal yearlyAmtLimitCr;

	@Column(name="YEARLY_AMT_LIMIT_DR")
	private BigDecimal yearlyAmtLimitDr;

	@Column(name="YEARLY_TRANS_LIMIT_CR")
	private BigDecimal yearlyTransLimitCr;

	@Column(name="YEARLY_TRANS_LIMIT_DR")
	private BigDecimal yearlyTransLimitDr;

	@Column(name="PROVINCE_ID")
	private BigDecimal provinceId;

	@PreUpdate
	public void incrementUpdateIndex() {
		if (this.updateindex == null) {
			this.updateindex = BigDecimal.ONE;
		} else {
			this.updateindex = this.updateindex.add(BigDecimal.ONE);
		}
	}
}