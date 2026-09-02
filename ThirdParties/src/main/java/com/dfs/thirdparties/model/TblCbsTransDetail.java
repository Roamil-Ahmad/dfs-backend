package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_CBS_TRANS_DETAIL database table.
 * 
 */
@Entity
@Table(name="TBL_CBS_TRANS_DETAIL")
@NamedQuery(name="TblCbsTransDetail.findAll", query="SELECT t FROM TblCbsTransDetail t")
public class TblCbsTransDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CBS_TRANS_DETAIL_CBSTRANSDETAILID_GENERATOR", sequenceName="TBL_CBS_TRANS_DETAIL_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CBS_TRANS_DETAIL_CBSTRANSDETAILID_GENERATOR")
	@Column(name="CBS_TRANS_DETAIL_ID")
	private long cbsTransDetailId;

	private BigDecimal amount;

	@Column(name="AMOUNT_TYPE")
	private String amountType;

	@Column(name="CBS_GL_ACCOUNT_CODE")
	private String cbsGlAccountCode;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblCbsTransHead
	@ManyToOne
	@JoinColumn(name="CBS_TRANS_HEAD_ID")
	private TblCbsTransHead tblCbsTransHead;

	public TblCbsTransDetail() {
	}

	public long getCbsTransDetailId() {
		return this.cbsTransDetailId;
	}

	public void setCbsTransDetailId(long cbsTransDetailId) {
		this.cbsTransDetailId = cbsTransDetailId;
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

	public String getCbsGlAccountCode() {
		return this.cbsGlAccountCode;
	}

	public void setCbsGlAccountCode(String cbsGlAccountCode) {
		this.cbsGlAccountCode = cbsGlAccountCode;
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

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public TblCbsTransHead getTblCbsTransHead() {
		return this.tblCbsTransHead;
	}

	public void setTblCbsTransHead(TblCbsTransHead tblCbsTransHead) {
		this.tblCbsTransHead = tblCbsTransHead;
	}

}