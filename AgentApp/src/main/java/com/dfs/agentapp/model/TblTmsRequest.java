package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_TMS_REQUEST database table.
 * 
 */
@Entity
@Table(name="TBL_TMS_REQUEST")
@NamedQuery(name="TblTmsRequest.findAll", query="SELECT t FROM TblTmsRequest t")
public class TblTmsRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TMS_REQUEST_TMSREQUESTID_GENERATOR", sequenceName="TBL_TMS_REQUEST_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TMS_REQUEST_TMSREQUESTID_GENERATOR")
	@Column(name="TMS_REQUEST_ID")
	private long tmsRequestId;

	private BigDecimal alertid;

	private String branchcode;

	private String clientid;

	private Date createdate;

	private BigDecimal createuser;

	private String customerdob;

	private String customerid;

	private String customername;

	private String status;

	//bi-directional many-to-one association to TblTmsResponse
	@OneToMany(mappedBy="tblTmsRequest")
	private List<TblTmsResponse> tblTmsResponses;

	public TblTmsRequest() {
	}

	public long getTmsRequestId() {
		return this.tmsRequestId;
	}

	public void setTmsRequestId(long tmsRequestId) {
		this.tmsRequestId = tmsRequestId;
	}

	public BigDecimal getAlertid() {
		return this.alertid;
	}

	public void setAlertid(BigDecimal alertid) {
		this.alertid = alertid;
	}

	public String getBranchcode() {
		return this.branchcode;
	}

	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}

	public String getClientid() {
		return this.clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
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

	public String getCustomerdob() {
		return this.customerdob;
	}

	public void setCustomerdob(String customerdob) {
		this.customerdob = customerdob;
	}

	public String getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getCustomername() {
		return this.customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<TblTmsResponse> getTblTmsResponses() {
		return this.tblTmsResponses;
	}

	public void setTblTmsResponses(List<TblTmsResponse> tblTmsResponses) {
		this.tblTmsResponses = tblTmsResponses;
	}

	public TblTmsResponse addTblTmsRespons(TblTmsResponse tblTmsRespons) {
		getTblTmsResponses().add(tblTmsRespons);
		tblTmsRespons.setTblTmsRequest(this);

		return tblTmsRespons;
	}

	public TblTmsResponse removeTblTmsRespons(TblTmsResponse tblTmsRespons) {
		getTblTmsResponses().remove(tblTmsRespons);
		tblTmsRespons.setTblTmsRequest(null);

		return tblTmsRespons;
	}

}