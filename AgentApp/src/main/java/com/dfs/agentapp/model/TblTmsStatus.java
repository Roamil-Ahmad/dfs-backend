package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_TMS_STATUS database table.
 * 
 */
@Entity
@Table(name="TBL_TMS_STATUS")
@NamedQuery(name="TblTmsStatus.findAll", query="SELECT t FROM TblTmsStatus t")
public class TblTmsStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TMS_STATUS_TMSSTATUSID_GENERATOR", sequenceName="TBL_TMS_STATUS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TMS_STATUS_TMSSTATUSID_GENERATOR")
	@Column(name="TMS_STATUS_ID")
	private long tmsStatusId;

	@Column(name="ALERT_ID")
	private BigDecimal alertId;

	@Column(name="API_STATUS")
	private String apiStatus;

	@Column(name="API_STATUSDESCR")
	private String apiStatusdescr;

	private String branchcode;

	@Column(name="CASHIN_TYPE")
	private String cashinType;
	@Column(name="NID_NO")

	private String cnic;

	private Date createdate;

	private BigDecimal createuser;

	private Date dob;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private String name;

	private String remarks;

	private String status;

	private BigDecimal threshold;

	private BigDecimal updateindex;

	public TblTmsStatus() {
	}

	public long getTmsStatusId() {
		return this.tmsStatusId;
	}

	public void setTmsStatusId(long tmsStatusId) {
		this.tmsStatusId = tmsStatusId;
	}

	public BigDecimal getAlertId() {
		return this.alertId;
	}

	public void setAlertId(BigDecimal alertId) {
		this.alertId = alertId;
	}

	public String getApiStatus() {
		return this.apiStatus;
	}

	public void setApiStatus(String apiStatus) {
		this.apiStatus = apiStatus;
	}

	public String getApiStatusdescr() {
		return this.apiStatusdescr;
	}

	public void setApiStatusdescr(String apiStatusdescr) {
		this.apiStatusdescr = apiStatusdescr;
	}

	public String getBranchcode() {
		return this.branchcode;
	}

	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}

	public String getCashinType() {
		return this.cashinType;
	}

	public void setCashinType(String cashinType) {
		this.cashinType = cashinType;
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

	public Date getDob() {
		return this.dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getThreshold() {
		return this.threshold;
	}

	public void setThreshold(BigDecimal threshold) {
		this.threshold = threshold;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}