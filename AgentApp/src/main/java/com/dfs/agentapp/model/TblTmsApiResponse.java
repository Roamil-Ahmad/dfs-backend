package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_TMS_API_RESPONSE database table.
 * 
 */
@Entity
@Table(name="TBL_TMS_API_RESPONSE")
@NamedQuery(name="TblTmsApiResponse.findAll", query="SELECT t FROM TblTmsApiResponse t")
public class TblTmsApiResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TMS_API_RESPONSE_TMSAPIRESPONSEID_GENERATOR", sequenceName="TBL_TMS_API_RESPONSE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TMS_API_RESPONSE_TMSAPIRESPONSEID_GENERATOR")
	@Column(name="TMS_API_RESPONSE_ID")
	private long tmsApiResponseId;

	@Column(name="ALERT_ID")
	private BigDecimal alertId;

	private Date createdate;

	private BigDecimal createuser;

	private String status;

	private String statusdescr;

	private BigDecimal threshold;

	//bi-directional many-to-one association to TblTmsApiRequest
	@ManyToOne
	@JoinColumn(name="TMS_API_REQUEST_ID")
	private TblTmsApiRequest tblTmsApiRequest;

	public TblTmsApiResponse() {
	}

	public long getTmsApiResponseId() {
		return this.tmsApiResponseId;
	}

	public void setTmsApiResponseId(long tmsApiResponseId) {
		this.tmsApiResponseId = tmsApiResponseId;
	}

	public BigDecimal getAlertId() {
		return this.alertId;
	}

	public void setAlertId(BigDecimal alertId) {
		this.alertId = alertId;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusdescr() {
		return this.statusdescr;
	}

	public void setStatusdescr(String statusdescr) {
		this.statusdescr = statusdescr;
	}

	public BigDecimal getThreshold() {
		return this.threshold;
	}

	public void setThreshold(BigDecimal threshold) {
		this.threshold = threshold;
	}

	public TblTmsApiRequest getTblTmsApiRequest() {
		return this.tblTmsApiRequest;
	}

	public void setTblTmsApiRequest(TblTmsApiRequest tblTmsApiRequest) {
		this.tblTmsApiRequest = tblTmsApiRequest;
	}

}