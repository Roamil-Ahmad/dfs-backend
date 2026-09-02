package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_CNIC_BLOCK_LIST_TEMP database table.
 * 
 */
@Entity
@Table(name="TBL_CNIC_BLOCK_LIST_TEMP")
@NamedQuery(name="TblCnicBlockListTemp.findAll", query="SELECT t FROM TblCnicBlockListTemp t")
public class TblCnicBlockListTemp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CNIC_BLOCK_LIST_TEMP_CNICBLOCKLISTTEMPID_GENERATOR", sequenceName="TBL_CNIC_BLOCK_LIST_TEMP_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CNIC_BLOCK_LIST_TEMP_CNICBLOCKLISTTEMPID_GENERATOR")
	@Column(name="CNIC_BLOCK_LIST_TEMP_ID")
	private long cnicBlockListTempId;

	@Column(name="BATCH_NO")
	private String batchNo;

	private BigDecimal cnic;

	private String comments;

	private Date createdate;

	private BigDecimal createuser;

	private String filename;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	private String name;

	private String reason;

	private String status;

	private BigDecimal updateindex;

	public TblCnicBlockListTemp() {
	}

	public long getCnicBlockListTempId() {
		return this.cnicBlockListTempId;
	}

	public void setCnicBlockListTempId(long cnicBlockListTempId) {
		this.cnicBlockListTempId = cnicBlockListTempId;
	}

	public String getBatchNo() {
		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public BigDecimal getCnic() {
		return this.cnic;
	}

	public void setCnic(BigDecimal cnic) {
		this.cnic = cnic;
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

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}