package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_TMS_RESPONSE database table.
 * 
 */
@Entity
@Table(name="TBL_TMS_RESPONSE")
@NamedQuery(name="TblTmsResponse.findAll", query="SELECT t FROM TblTmsResponse t")
public class TblTmsResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TMS_RESPONSE_TMSRESPONSEID_GENERATOR", sequenceName="TBL_TMS_RESPONSE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TMS_RESPONSE_TMSRESPONSEID_GENERATOR")
	@Column(name="TMS_RESPONSE_ID")
	private long tmsResponseId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="RESPONSE_CODE")
	private String responseCode;

	@Column(name="RESPONSE_DESCR")
	private String responseDescr;

	//bi-directional many-to-one association to TblTmsRequest
	@ManyToOne
	@JoinColumn(name="TMS_REQUEST_ID")
	private TblTmsRequest tblTmsRequest;

	public TblTmsResponse() {
	}

	public long getTmsResponseId() {
		return this.tmsResponseId;
	}

	public void setTmsResponseId(long tmsResponseId) {
		this.tmsResponseId = tmsResponseId;
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

	public String getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDescr() {
		return this.responseDescr;
	}

	public void setResponseDescr(String responseDescr) {
		this.responseDescr = responseDescr;
	}

	public TblTmsRequest getTblTmsRequest() {
		return this.tblTmsRequest;
	}

	public void setTblTmsRequest(TblTmsRequest tblTmsRequest) {
		this.tblTmsRequest = tblTmsRequest;
	}

}