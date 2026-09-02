package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_NID_BLOCK_LIST_TEMP database table.
 * 
 */
@Entity
@Table(name="TBL_NID_BLOCK_LIST_TEMP")
@NamedQuery(name="TblNidBlockListTemp.findAll", query="SELECT t FROM TblNidBlockListTemp t")
public class TblNidBlockListTemp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_NID_BLOCK_LIST_TEMP_NIDBLOCKLISTTEMPID_GENERATOR", sequenceName="TBL_CNIC_BLOCK_LIST_TEMP_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_NID_BLOCK_LIST_TEMP_NIDBLOCKLISTTEMPID_GENERATOR")
	@Column(name="NID_BLOCK_LIST_TEMP_ID")
	private long nidBlockListTempId;

	@Column(name="BATCH_NO")
	private String batchNo;

	private String comments;

	private Timestamp createdate;

	private BigDecimal createuser;

	private String filename;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	private String name;

	private String reason;

	private String status;

	@Column(name="NID_NO")
	private BigDecimal nidNo;

	private BigDecimal updateindex;

	public TblNidBlockListTemp() {
	}

	public long getNidBlockListTempId() {
		return this.nidBlockListTempId;
	}

	public void setNidBlockListTempId(long nidBlockListTempId) {
		this.nidBlockListTempId = nidBlockListTempId;
	}

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Timestamp getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Timestamp createdate) {
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

	public Timestamp getLastupdatedate() {
		return this.lastupdatedate;
	}

	public void setLastupdatedate(Timestamp lastupdatedate) {
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getNidNo() {
		return this.nidNo;
	}

	public void setNidNo(BigDecimal nidNo) {
		this.nidNo = nidNo;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}