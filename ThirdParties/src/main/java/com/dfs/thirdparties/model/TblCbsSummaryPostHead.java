package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_CBS_SUMMARY_POST_HEAD database table.
 * 
 */
@Entity
@Table(name="TBL_CBS_SUMMARY_POST_HEAD")
@NamedQuery(name="TblCbsSummaryPostHead.findAll", query="SELECT t FROM TblCbsSummaryPostHead t")
public class TblCbsSummaryPostHead implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CBS_SUMMARY_POST_HEAD_CBSSUMMARYPOSTHEADID_GENERATOR", sequenceName="TBL_CBS_SUMMARY_POST_HEAD_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CBS_SUMMARY_POST_HEAD_CBSSUMMARYPOSTHEADID_GENERATOR")
	@Column(name="CBS_SUMMARY_POST_HEAD_ID")
	private long cbsSummaryPostHeadId;

	@Column(name="BATCH_NO")
	private String batchNo;

	@Column(name="CBS_BATCH_NO")
	private String cbsBatchNo;

	@Column(name="CBS_TRANS_DATE")
	private Date cbsTransDate;

	@Column(name="CBS_TRANS_REFNUM")
	private String cbsTransRefnum;

	private String comments;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="FROM_DATE")
	private Date fromDate;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private String status;

	@Column(name="TO_DATE")
	private Date toDate;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblCbsSummaryPostDetail
	@OneToMany(mappedBy="tblCbsSummaryPostHead")
	private List<TblCbsSummaryPostDetail> tblCbsSummaryPostDetails;

	//bi-directional many-to-one association to TblTransDoc
	@ManyToOne
	@JoinColumn(name="TRANS_DOCS_ID")
	private TblTransDoc tblTransDoc;

	public TblCbsSummaryPostHead() {
	}

	public long getCbsSummaryPostHeadId() {
		return this.cbsSummaryPostHeadId;
	}

	public void setCbsSummaryPostHeadId(long cbsSummaryPostHeadId) {
		this.cbsSummaryPostHeadId = cbsSummaryPostHeadId;
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

	public Object getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
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

	public Object getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public List<TblCbsSummaryPostDetail> getTblCbsSummaryPostDetails() {
		return this.tblCbsSummaryPostDetails;
	}

	public void setTblCbsSummaryPostDetails(List<TblCbsSummaryPostDetail> tblCbsSummaryPostDetails) {
		this.tblCbsSummaryPostDetails = tblCbsSummaryPostDetails;
	}

	public TblCbsSummaryPostDetail addTblCbsSummaryPostDetail(TblCbsSummaryPostDetail tblCbsSummaryPostDetail) {
		getTblCbsSummaryPostDetails().add(tblCbsSummaryPostDetail);
		tblCbsSummaryPostDetail.setTblCbsSummaryPostHead(this);

		return tblCbsSummaryPostDetail;
	}

	public TblCbsSummaryPostDetail removeTblCbsSummaryPostDetail(TblCbsSummaryPostDetail tblCbsSummaryPostDetail) {
		getTblCbsSummaryPostDetails().remove(tblCbsSummaryPostDetail);
		tblCbsSummaryPostDetail.setTblCbsSummaryPostHead(null);

		return tblCbsSummaryPostDetail;
	}

	public TblTransDoc getTblTransDoc() {
		return this.tblTransDoc;
	}

	public void setTblTransDoc(TblTransDoc tblTransDoc) {
		this.tblTransDoc = tblTransDoc;
	}

}