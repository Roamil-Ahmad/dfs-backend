package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the TBL_BULK_ACCOUNT_FILE_HEAD database table.
 * 
 */
@Entity
@Table(name="TBL_BULK_ACCOUNT_FILE_HEAD")
@NamedQuery(name="TblBulkAccountFileHead.findAll", query="SELECT t FROM TblBulkAccountFileHead t")
public class TblBulkAccountFileHead implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_BULK_ACCOUNT_FILE_HEAD_BULKACCOUNTFILEHEADID_GENERATOR", sequenceName="TBL_BULK_ACCOUNT_FILE_HEAD_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_BULK_ACCOUNT_FILE_HEAD_BULKACCOUNTFILEHEADID_GENERATOR")
	@Column(name="BULK_ACCOUNT_FILE_HEAD_ID")
	private long bulkAccountFileHeadId;

	private Date column3;

	private Date createdate;

	private BigDecimal createuser;

	private String filename;

	private BigDecimal lastupdateuser;

	private String status;

	@Column(name="TOTAL_RECORDS")
	private BigDecimal totalRecords;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblBulkAccountFileDetail
	@OneToMany(mappedBy="tblBulkAccountFileHead")
	private List<TblBulkAccountFileDetail> tblBulkAccountFileDetails;

	public TblBulkAccountFileHead() {
	}

	public long getBulkAccountFileHeadId() {
		return this.bulkAccountFileHeadId;
	}

	public void setBulkAccountFileHeadId(long bulkAccountFileHeadId) {
		this.bulkAccountFileHeadId = bulkAccountFileHeadId;
	}

	public Date getColumn3() {
		return this.column3;
	}

	public void setColumn3(Date column3) {
		this.column3 = column3;
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

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

	public BigDecimal getTotalRecords() {
		return this.totalRecords;
	}

	public void setTotalRecords(BigDecimal totalRecords) {
		this.totalRecords = totalRecords;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public List<TblBulkAccountFileDetail> getTblBulkAccountFileDetails() {
		return this.tblBulkAccountFileDetails;
	}

	public void setTblBulkAccountFileDetails(List<TblBulkAccountFileDetail> tblBulkAccountFileDetails) {
		this.tblBulkAccountFileDetails = tblBulkAccountFileDetails;
	}

	public TblBulkAccountFileDetail addTblBulkAccountFileDetail(TblBulkAccountFileDetail tblBulkAccountFileDetail) {
		getTblBulkAccountFileDetails().add(tblBulkAccountFileDetail);
		tblBulkAccountFileDetail.setTblBulkAccountFileHead(this);

		return tblBulkAccountFileDetail;
	}

	public TblBulkAccountFileDetail removeTblBulkAccountFileDetail(TblBulkAccountFileDetail tblBulkAccountFileDetail) {
		getTblBulkAccountFileDetails().remove(tblBulkAccountFileDetail);
		tblBulkAccountFileDetail.setTblBulkAccountFileHead(null);

		return tblBulkAccountFileDetail;
	}

}