package com.barq.nadra.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_NID_HITS database table.
 * 
 */
@Entity
@Table(name="TBL_NID_HITS")
@NamedQuery(name="TblNidHits.findAll", query="SELECT t FROM TblNidHits t")
public class TblNidHits implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_NID_HITS_NIDHITSID_GENERATOR", sequenceName="TBL_NID_HITS_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_NID_HITS_NIDHITSID_GENERATOR")
	@Column(name="NID_HITS_ID")
	private long nidHitsId;

	@Column(name="NID_NO")
	private String nidNo;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Column(name="RESPONSE")
	private String response;

	@Column(name="RESPONSE_CODE")
	private String responseCode;

	@Column(name="RESPONSE_MESSAGE")
	private String responseMessage;

	@Column(name="SERVICE_NAME")
	private String serviceName;

	@Column(name="SESSION_ID")
	private String sessionId;

	@Column(name="TRANSACTION_ID")
	private String transactionId;

	public TblNidHits() {
	}

	public long getNidHitsId() {
		return this.nidHitsId;
	}

	public void setNidHitsId(long nidHitsId) {
		this.nidHitsId = nidHitsId;
	}

	public String getNidNo() {
		return this.nidNo;
	}

	public void setNidNo(String nidNo) {
		this.nidNo = nidNo;
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

	public String getResponse() {
		return this.response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return this.responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}