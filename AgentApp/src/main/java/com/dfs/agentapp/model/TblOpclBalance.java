package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_OPCL_BALANCE database table.
 * 
 */
@Entity
@Table(name="TBL_OPCL_BALANCE")
@NamedQuery(name="TblOpclBalance.findAll", query="SELECT t FROM TblOpclBalance t")
public class TblOpclBalance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_OPCL_BALANCE_OPCLBALANCEID_GENERATOR", sequenceName="TBL_OPCL_BALANCE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_OPCL_BALANCE_OPCLBALANCEID_GENERATOR")
	@Column(name="OPCL_BALANCE_ID")
	private long opclBalanceId;

	@Column(name="ACCOUNT_REF_ID")
	private BigDecimal accountRefId;

	@Column(name="ACCOUNT_REF_TYPE")
	private String accountRefType;

	private BigDecimal amount;

	@Column(name="AMOUNT_TYPE")
	private String amountType;

	@Column(name="BALANCE_DATE")
	private Date balanceDate;

	@Column(name="CL_BALANCE")
	private BigDecimal clBalance;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="OP_BALANCE")
	private BigDecimal opBalance;

	//bi-directional many-to-one association to TblTransDetail
	@ManyToOne
	@JoinColumn(name="TRANS_DETAIL_ID")
	private TblTransDetail tblTransDetail;

	//bi-directional many-to-one association to TblTransHead
	@ManyToOne
	@JoinColumn(name="TRANS_HEAD_ID")
	private TblTransHead tblTransHead;

	public TblOpclBalance() {
	}

	public long getOpclBalanceId() {
		return this.opclBalanceId;
	}

	public void setOpclBalanceId(long opclBalanceId) {
		this.opclBalanceId = opclBalanceId;
	}

	public BigDecimal getAccountRefId() {
		return this.accountRefId;
	}

	public void setAccountRefId(BigDecimal accountRefId) {
		this.accountRefId = accountRefId;
	}

	public String getAccountRefType() {
		return this.accountRefType;
	}

	public void setAccountRefType(String accountRefType) {
		this.accountRefType = accountRefType;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getAmountType() {
		return this.amountType;
	}

	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}

	public Object getBalanceDate() {
		return this.balanceDate;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
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

	public BigDecimal getOpBalance() {
		return this.opBalance;
	}

	public void setOpBalance(BigDecimal opBalance) {
		this.opBalance = opBalance;
	}

	public TblTransDetail getTblTransDetail() {
		return this.tblTransDetail;
	}

	public void setTblTransDetail(TblTransDetail tblTransDetail) {
		this.tblTransDetail = tblTransDetail;
	}

	public TblTransHead getTblTransHead() {
		return this.tblTransHead;
	}

	public void setTblTransHead(TblTransHead tblTransHead) {
		this.tblTransHead = tblTransHead;
	}

}