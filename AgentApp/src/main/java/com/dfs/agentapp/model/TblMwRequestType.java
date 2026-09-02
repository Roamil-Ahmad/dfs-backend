package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_MW_REQUEST_TYPE database table.
 * 
 */
@Entity
@Table(name="TBL_MW_REQUEST_TYPE")
@NamedQuery(name="TblMwRequestType.findAll", query="SELECT t FROM TblMwRequestType t")
public class TblMwRequestType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_MW_REQUEST_TYPE_MWREQUESTTYPEID_GENERATOR", sequenceName="TBL_MW_REQUEST_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MW_REQUEST_TYPE_MWREQUESTTYPEID_GENERATOR")
	@Column(name="MW_REQUEST_TYPE_ID")
	private long mwRequestTypeId;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MW_REQUEST_TYPE_CODE")
	private String mwRequestTypeCode;

	@Column(name="MW_REQUEST_TYPE_DESCR")
	private String mwRequestTypeDescr;

	private String status;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblMwRequest
	@OneToMany(mappedBy="tblMwRequestType")
	private List<TblMwRequest> tblMwRequests;

	public TblMwRequestType() {
	}

	public long getMwRequestTypeId() {
		return this.mwRequestTypeId;
	}

	public void setMwRequestTypeId(long mwRequestTypeId) {
		this.mwRequestTypeId = mwRequestTypeId;
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

	public String getMwRequestTypeCode() {
		return this.mwRequestTypeCode;
	}

	public void setMwRequestTypeCode(String mwRequestTypeCode) {
		this.mwRequestTypeCode = mwRequestTypeCode;
	}

	public String getMwRequestTypeDescr() {
		return this.mwRequestTypeDescr;
	}

	public void setMwRequestTypeDescr(String mwRequestTypeDescr) {
		this.mwRequestTypeDescr = mwRequestTypeDescr;
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

	public List<TblMwRequest> getTblMwRequests() {
		return this.tblMwRequests;
	}

	public void setTblMwRequests(List<TblMwRequest> tblMwRequests) {
		this.tblMwRequests = tblMwRequests;
	}

	public TblMwRequest addTblMwRequest(TblMwRequest tblMwRequest) {
		getTblMwRequests().add(tblMwRequest);
		tblMwRequest.setTblMwRequestType(this);

		return tblMwRequest;
	}

	public TblMwRequest removeTblMwRequest(TblMwRequest tblMwRequest) {
		getTblMwRequests().remove(tblMwRequest);
		tblMwRequest.setTblMwRequestType(null);

		return tblMwRequest;
	}

}