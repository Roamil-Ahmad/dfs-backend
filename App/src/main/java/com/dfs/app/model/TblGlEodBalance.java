package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_GL_EOD_BALANCE database table.
 * 
 */
@Entity
@Table(name="TBL_GL_EOD_BALANCE")
@NamedQuery(name="TblGlEodBalance.findAll", query="SELECT t FROM TblGlEodBalance t")
public class TblGlEodBalance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_GL_EOD_BALANCE_GLEODBALANCEID_GENERATOR", sequenceName="TBL_GL_EOD_BALANCE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_GL_EOD_BALANCE_GLEODBALANCEID_GENERATOR")
	@Column(name="GL_EOD_BALANCE_ID")
	private long glEodBalanceId;

	private Date asondate;

	@Column(name="CL_BALANCE")
	private BigDecimal clBalance;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="GL_ACCOUNT_ID")
	private BigDecimal glAccountId;

	@Column(name="OP_BALANCE")
	private BigDecimal opBalance;

	@Column(name="TOTAL_CR")
	private BigDecimal totalCr;

	@Column(name="TOTAL_DR")
	private BigDecimal totalDr;

	public TblGlEodBalance() {
	}

	public long getGlEodBalanceId() {
		return this.glEodBalanceId;
	}

	public void setGlEodBalanceId(long glEodBalanceId) {
		this.glEodBalanceId = glEodBalanceId;
	}

	public Date getAsondate() {
		return this.asondate;
	}

	public void setAsondate(Date asondate) {
		this.asondate = asondate;
	}

	public BigDecimal getClBalance() {
		return this.clBalance;
	}

	public void setClBalance(BigDecimal clBalance) {
		this.clBalance = clBalance;
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

	public BigDecimal getGlAccountId() {
		return this.glAccountId;
	}

	public void setGlAccountId(BigDecimal glAccountId) {
		this.glAccountId = glAccountId;
	}

	public BigDecimal getOpBalance() {
		return this.opBalance;
	}

	public void setOpBalance(BigDecimal opBalance) {
		this.opBalance = opBalance;
	}

	public BigDecimal getTotalCr() {
		return this.totalCr;
	}

	public void setTotalCr(BigDecimal totalCr) {
		this.totalCr = totalCr;
	}

	public BigDecimal getTotalDr() {
		return this.totalDr;
	}

	public void setTotalDr(BigDecimal totalDr) {
		this.totalDr = totalDr;
	}

}