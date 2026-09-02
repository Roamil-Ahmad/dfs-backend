package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_TRANS_DETAIL database table.
 * 
 */
@Entity
@Table(name="TBL_TRANS_DETAIL")
@NamedQuery(name="TblTransDetail.findAll", query="SELECT t FROM TblTransDetail t")
public class TblTransDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TRANS_DETAIL_TRANSDETAILID_GENERATOR", sequenceName="TBL_TRANS_DETAIL_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TRANS_DETAIL_TRANSDETAILID_GENERATOR")
	@Column(name="TRANS_DETAIL_ID")
	private long transDetailId;

	@Column(name="ACCOUNT_REF_ID")
	private BigDecimal accountRefId;

	@Column(name="ACCOUNT_REF_TYPE")
	private String accountRefType;

	private BigDecimal amount;

	@Column(name="AMOUNT_TYPE")
	private String amountType;

	@Column(name="CL_BALANCE")
	private BigDecimal clBalance;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="OP_BALANCE")
	private BigDecimal opBalance;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblOpclBalance
	@OneToMany(mappedBy="tblTransDetail")
	private List<TblOpclBalance> tblOpclBalances;

	//bi-directional many-to-one association to LkpCharge
	@ManyToOne
	@JoinColumn(name="CHARGES_ID")
	private LkpCharge lkpCharge;

	//bi-directional many-to-one association to TblTransHead
	@ManyToOne
	@JoinColumn(name="TRANS_HEAD_ID")
	private TblTransHead tblTransHead;

	public TblTransDetail() {
	}

	public long getTransDetailId() {
		return this.transDetailId;
	}

	public void setTransDetailId(long transDetailId) {
		this.transDetailId = transDetailId;
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

	public BigDecimal getOpBalance() {
		return this.opBalance;
	}

	public void setOpBalance(BigDecimal opBalance) {
		this.opBalance = opBalance;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public List<TblOpclBalance> getTblOpclBalances() {
		return this.tblOpclBalances;
	}

	public void setTblOpclBalances(List<TblOpclBalance> tblOpclBalances) {
		this.tblOpclBalances = tblOpclBalances;
	}

	public TblOpclBalance addTblOpclBalance(TblOpclBalance tblOpclBalance) {
		getTblOpclBalances().add(tblOpclBalance);
		tblOpclBalance.setTblTransDetail(this);

		return tblOpclBalance;
	}

	public TblOpclBalance removeTblOpclBalance(TblOpclBalance tblOpclBalance) {
		getTblOpclBalances().remove(tblOpclBalance);
		tblOpclBalance.setTblTransDetail(null);

		return tblOpclBalance;
	}

	public LkpCharge getLkpCharge() {
		return this.lkpCharge;
	}

	public void setLkpCharge(LkpCharge lkpCharge) {
		this.lkpCharge = lkpCharge;
	}

	public TblTransHead getTblTransHead() {
		return this.tblTransHead;
	}

	public void setTblTransHead(TblTransHead tblTransHead) {
		this.tblTransHead = tblTransHead;
	}

}