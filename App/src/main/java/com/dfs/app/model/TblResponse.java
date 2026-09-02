package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_RESPONSE database table.
 * 
 */
@Entity
@Table(name="TBL_RESPONSE")
@NamedQuery(name="TblResponse.findAll", query="SELECT t FROM TblResponse t")
public class TblResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_RESPONSE_RESPONSEID_GENERATOR", sequenceName="TBL_RESPONSE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_RESPONSE_RESPONSEID_GENERATOR")
	@Column(name="RESPONSE_ID")
	private long responseId;

	@Column(name="ACCOUNT_NO")
	private String accountNo;

	@Column(name="ACCOUNT_TITLE")
	private String accountTitle;

	@Column(name="ADDITIONAL_DATA")
	private String additionalData;

	private String address;

	@Column(name="AUTH_ID_RESPONSE")
	private String authIdResponse;

	@Column(name="BENEFICIARY_IBAN")
	private String beneficiaryIban;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="CUSTOMER_NAME")
	private String customerName;

	@Column(name="DC_CHARGES")
	private BigDecimal dcCharges;

	@Column(name="DC_CHARGES_WITH_DELIVERY")
	private BigDecimal dcChargesWithDelivery;

	@Column(name="RESPONSE_CODE")
	private String responseCode;

	@Column(name="RRN")
	private String rrn;

	private String stan;

	@Column(name="TRANS_DATE")
	private String transDate;

	@Column(name="TRANS_DESCR")
	private String transDescr;

	@Column(name = "RESPONSE_JSON")
	private String responseJson;

	private String utilitycompanyid;

	//bi-directional many-to-one association to TblRequest
	@ManyToOne
	@JoinColumn(name="REQUEST_ID")
	private TblRequest tblRequest;

	public TblResponse() {
	}

	public long getResponseId() {
		return this.responseId;
	}

	public void setResponseId(long responseId) {
		this.responseId = responseId;
	}

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountTitle() {
		return this.accountTitle;
	}

	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}

	public String getAdditionalData() {
		return this.additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAuthIdResponse() {
		return this.authIdResponse;
	}

	public void setAuthIdResponse(String authIdResponse) {
		this.authIdResponse = authIdResponse;
	}

	public String getBeneficiaryIban() {
		return this.beneficiaryIban;
	}

	public void setBeneficiaryIban(String beneficiaryIban) {
		this.beneficiaryIban = beneficiaryIban;
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

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public BigDecimal getDcCharges() {
		return this.dcCharges;
	}

	public void setDcCharges(BigDecimal dcCharges) {
		this.dcCharges = dcCharges;
	}

	public BigDecimal getDcChargesWithDelivery() {
		return this.dcChargesWithDelivery;
	}

	public void setDcChargesWithDelivery(BigDecimal dcChargesWithDelivery) {
		this.dcChargesWithDelivery = dcChargesWithDelivery;
	}

	public String getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getRrn() {
		return this.rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getStan() {
		return this.stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	public String getTransDate() {
		return this.transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getTransDescr() {
		return this.transDescr;
	}

	public void setTransDescr(String transDescr) {
		this.transDescr = transDescr;
	}

	public String getUtilitycompanyid() {
		return this.utilitycompanyid;
	}

	public void setUtilitycompanyid(String utilitycompanyid) {
		this.utilitycompanyid = utilitycompanyid;
	}

	public TblRequest getTblRequest() {
		return this.tblRequest;
	}

	public void setTblRequest(TblRequest tblRequest) {
		this.tblRequest = tblRequest;
	}

	public String getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}
}