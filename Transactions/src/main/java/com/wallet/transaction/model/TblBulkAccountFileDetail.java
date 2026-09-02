package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_BULK_ACCOUNT_FILE_DETAIL database table.
 * 
 */
@Entity
@Table(name="TBL_BULK_ACCOUNT_FILE_DETAIL")
@NamedQuery(name="TblBulkAccountFileDetail.findAll", query="SELECT t FROM TblBulkAccountFileDetail t")
public class TblBulkAccountFileDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_BULK_ACCOUNT_FILE_DETAIL_BULKACCOUNTFILEDETAILID_GENERATOR", sequenceName="TBL_BULK_ACCOUNT_FILE_DETAIL_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_BULK_ACCOUNT_FILE_DETAIL_BULKACCOUNTFILEDETAILID_GENERATOR")
	@Column(name="BULK_ACCOUNT_FILE_DETAIL_ID")
	private long bulkAccountFileDetailId;
	@Column(name="NID_NO")

	private String cnic;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="ISSUANCE_DATE")
	private String issuanceDate;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	private String status;

	@Column(name="STATUS_DESCR")
	private String statusDescr;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblBulkAccountFileHead
	@ManyToOne
	@JoinColumn(name="BULK_ACCOUNT_FILE_HEAD_ID")
	private TblBulkAccountFileHead tblBulkAccountFileHead;

	public TblBulkAccountFileDetail() {
	}

	public long getBulkAccountFileDetailId() {
		return this.bulkAccountFileDetailId;
	}

	public void setBulkAccountFileDetailId(long bulkAccountFileDetailId) {
		this.bulkAccountFileDetailId = bulkAccountFileDetailId;
	}

	public String getCnic() {
		return this.cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
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

	public String getIssuanceDate() {
		return this.issuanceDate;
	}

	public void setIssuanceDate(String issuanceDate) {
		this.issuanceDate = issuanceDate;
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

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDescr() {
		return this.statusDescr;
	}

	public void setStatusDescr(String statusDescr) {
		this.statusDescr = statusDescr;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public TblBulkAccountFileHead getTblBulkAccountFileHead() {
		return this.tblBulkAccountFileHead;
	}

	public void setTblBulkAccountFileHead(TblBulkAccountFileHead tblBulkAccountFileHead) {
		this.tblBulkAccountFileHead = tblBulkAccountFileHead;
	}

}