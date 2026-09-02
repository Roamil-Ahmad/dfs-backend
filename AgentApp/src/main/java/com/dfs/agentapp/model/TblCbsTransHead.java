package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_CBS_TRANS_HEAD database table.
 * 
 */
@Entity
@Table(name="TBL_CBS_TRANS_HEAD")
@NamedQuery(name="TblCbsTransHead.findAll", query="SELECT t FROM TblCbsTransHead t")
public class TblCbsTransHead implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CBS_TRANS_HEAD_CBSTRANSHEADID_GENERATOR", sequenceName="TBL_CBS_TRANS_HEAD_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CBS_TRANS_HEAD_CBSTRANSHEADID_GENERATOR")
	@Column(name="CBS_TRANS_HEAD_ID")
	private long cbsTransHeadId;

	@Column(name="BATCH_NO")
	private String batchNo;

	@Column(name="CBS_BATCH_NO")
	private String cbsBatchNo;

	@Column(name="CBS_TRANS_DATE")
	private Date cbsTransDate;

	@Column(name="CBS_TRANS_REFNUM")
	private String cbsTransRefnum;

	@Column(name="CHARGES_PROFILE_FLAG")
	private String chargesProfileFlag;

	private String comments;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private String status;

	@Column(name="TRANS_HEAD_ID")
	private BigDecimal transHeadId;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblCbsTransDetail
	@OneToMany(mappedBy="tblCbsTransHead")
	private List<TblCbsTransDetail> tblCbsTransDetails;

	//bi-directional many-to-one association to TblTransDoc
	@ManyToOne
	@JoinColumn(name="TRANS_DOCS_ID")
	private TblTransDoc tblTransDoc;

	public TblCbsTransHead() {
	}

	public long getCbsTransHeadId() {
		return this.cbsTransHeadId;
	}

	public void setCbsTransHeadId(long cbsTransHeadId) {
		this.cbsTransHeadId = cbsTransHeadId;
	}

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getCbsBatchNo() {
		return this.cbsBatchNo;
	}

	public void setCbsBatchNo(String cbsBatchNo) {
		this.cbsBatchNo = cbsBatchNo;
	}

	public Object getCbsTransDate() {
		return this.cbsTransDate;
	}

	public void setCbsTransDate(Date cbsTransDate) {
		this.cbsTransDate = cbsTransDate;
	}

	public String getCbsTransRefnum() {
		return this.cbsTransRefnum;
	}

	public void setCbsTransRefnum(String cbsTransRefnum) {
		this.cbsTransRefnum = cbsTransRefnum;
	}

	public String getChargesProfileFlag() {
		return this.chargesProfileFlag;
	}

	public void setChargesProfileFlag(String chargesProfileFlag) {
		this.chargesProfileFlag = chargesProfileFlag;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getTransHeadId() {
		return this.transHeadId;
	}

	public void setTransHeadId(BigDecimal transHeadId) {
		this.transHeadId = transHeadId;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public List<TblCbsTransDetail> getTblCbsTransDetails() {
		return this.tblCbsTransDetails;
	}

	public void setTblCbsTransDetails(List<TblCbsTransDetail> tblCbsTransDetails) {
		this.tblCbsTransDetails = tblCbsTransDetails;
	}

	public TblCbsTransDetail addTblCbsTransDetail(TblCbsTransDetail tblCbsTransDetail) {
		getTblCbsTransDetails().add(tblCbsTransDetail);
		tblCbsTransDetail.setTblCbsTransHead(this);

		return tblCbsTransDetail;
	}

	public TblCbsTransDetail removeTblCbsTransDetail(TblCbsTransDetail tblCbsTransDetail) {
		getTblCbsTransDetails().remove(tblCbsTransDetail);
		tblCbsTransDetail.setTblCbsTransHead(null);

		return tblCbsTransDetail;
	}

	public TblTransDoc getTblTransDoc() {
		return this.tblTransDoc;
	}

	public void setTblTransDoc(TblTransDoc tblTransDoc) {
		this.tblTransDoc = tblTransDoc;
	}

}