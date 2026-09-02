package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_CBS_SUMMARY_POST_DETAIL database table.
 * 
 */
@Entity
@Table(name="TBL_CBS_SUMMARY_POST_DETAIL")
@NamedQuery(name="TblCbsSummaryPostDetail.findAll", query="SELECT t FROM TblCbsSummaryPostDetail t")
public class TblCbsSummaryPostDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CBS_SUMMARY_POST_DETAIL_CBSSUMMARYPOSTDETAILID_GENERATOR", sequenceName="TBL_CBS_SUMMARY_POST_DETAIL_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CBS_SUMMARY_POST_DETAIL_CBSSUMMARYPOSTDETAILID_GENERATOR")
	@Column(name="CBS_SUMMARY_POST_DETAIL_ID")
	private long cbsSummaryPostDetailId;

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

	//bi-directional many-to-one association to TblCbsSummaryPostHead
	@ManyToOne
	@JoinColumn(name="CBS_SUMMARY_POST_HEAD_ID")
	private TblCbsSummaryPostHead tblCbsSummaryPostHead;

	public TblCbsSummaryPostDetail() {
	}

	public long getCbsSummaryPostDetailId() {
		return this.cbsSummaryPostDetailId;
	}

	public void setCbsSummaryPostDetailId(long cbsSummaryPostDetailId) {
		this.cbsSummaryPostDetailId = cbsSummaryPostDetailId;
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

	public TblCbsSummaryPostHead getTblCbsSummaryPostHead() {
		return this.tblCbsSummaryPostHead;
	}

	public void setTblCbsSummaryPostHead(TblCbsSummaryPostHead tblCbsSummaryPostHead) {
		this.tblCbsSummaryPostHead = tblCbsSummaryPostHead;
	}

}