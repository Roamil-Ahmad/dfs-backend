package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_TMS_API_REQUEST database table.
 * 
 */
@Entity
@Table(name="TBL_TMS_API_REQUEST")
@NamedQuery(name="TblTmsApiRequest.findAll", query="SELECT t FROM TblTmsApiRequest t")
public class TblTmsApiRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TMS_API_REQUEST_TMSAPIREQUESTID_GENERATOR", sequenceName="TBL_TMS_API_REQUEST_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TMS_API_REQUEST_TMSAPIREQUESTID_GENERATOR")
	@Column(name="TMS_API_REQUEST_ID")
	private long tmsApiRequestId;

	private String branchcode;
	@Column(name="NID_NO")

	private String cnic;

	private Date createdate;

	private BigDecimal createuser;

	private String dob;

	@Column(name="FULL_NAME")
	private String fullName;

	@Column(name="REQUEST_URL")
	private String requestUrl;

	//bi-directional many-to-one association to TblTmsApiResponse
	@OneToMany(mappedBy="tblTmsApiRequest")
	private List<TblTmsApiResponse> tblTmsApiResponses;

	public TblTmsApiRequest() {
	}

	public long getTmsApiRequestId() {
		return this.tmsApiRequestId;
	}

	public void setTmsApiRequestId(long tmsApiRequestId) {
		this.tmsApiRequestId = tmsApiRequestId;
	}

	public String getBranchcode() {
		return this.branchcode;
	}

	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
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

	public String getDob() {
		return this.dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRequestUrl() {
		return this.requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public List<TblTmsApiResponse> getTblTmsApiResponses() {
		return this.tblTmsApiResponses;
	}

	public void setTblTmsApiResponses(List<TblTmsApiResponse> tblTmsApiResponses) {
		this.tblTmsApiResponses = tblTmsApiResponses;
	}

	public TblTmsApiResponse addTblTmsApiRespons(TblTmsApiResponse tblTmsApiRespons) {
		getTblTmsApiResponses().add(tblTmsApiRespons);
		tblTmsApiRespons.setTblTmsApiRequest(this);

		return tblTmsApiRespons;
	}

	public TblTmsApiResponse removeTblTmsApiRespons(TblTmsApiResponse tblTmsApiRespons) {
		getTblTmsApiResponses().remove(tblTmsApiRespons);
		tblTmsApiRespons.setTblTmsApiRequest(null);

		return tblTmsApiRespons;
	}

}