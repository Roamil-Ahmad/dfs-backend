package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_REQUEST database table.
 * 
 */
@Entity
@Table(name="TBL_REQUEST")
@NamedQuery(name="TblRequest.findAll", query="SELECT t FROM TblRequest t")
public class TblRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_REQUEST_REQUESTID_GENERATOR", sequenceName="TBL_REQUEST_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_REQUEST_REQUESTID_GENERATOR")
	@Column(name="REQUEST_ID")
	private long requestId;

	@Column(name="ACCOUNT_ID")
	private BigDecimal accountId;

	@Column(name="ACCOUNT_IMD")
	private String accountImd;

	@Column(name="ACCOUNT_NO")
	private String accountNo;

	@Column(name="ACCOUNT_TITLE")
	private String accountTitle;

	@Column(name="ACCOUNT_TYPE")
	private String accountType;

	@Column(name="ADD_BENEFICIARY")
	private String addBeneficiary;

	private String amount;

	@Column(name="AMOUNT_PAID")
	private String amountPaid;

	@Column(name="BENEFICIARY_EMAIL")
	private String beneficiaryEmail;

	@Column(name="BENEFICIARY_IBAN")
	private String beneficiaryIban;

	@Column(name="BENEFICIARY_MOBILE")
	private String beneficiaryMobile;

	@Column(name="BENEFICIARY_NAME")
	private String beneficiaryName;

	@Column(name="BRANCH_CODE")
	private String branchCode;

	@Column(name="BRANCH_NAME")
	private String branchName;

	@Column(name="CARD_ID")
	private String cardId;

	@Column(name="CARD_TYPE")
	private String cardType;

	private BigDecimal channels;

	@Column(name="NID_NO")
	private String cnic;

	private String comments;

	@Column(name="CONSENT_CHANNEL")
	private String consentChannel;

	@Column(name="CONSENT_STATUS")
	private String consentStatus;

	@Column(name="CONSENT_TRANSACTION")
	private String consentTransaction;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="CUSTOMER_ALL_ID")
	private BigDecimal customerAllId;

	@Column(name="CUSTOMER_NAME")
	private String customerName;

	@Column(name="DELIVERY_ADDRESS")
	private String deliveryAddress;

	@Column(name="DELIVERY_TYPE")
	private String deliveryType;

	@Column(name="DISPUTE_TYPE")
	private BigDecimal disputeType;

	@Column(name="END_POINT")
	private String endPoint;

	@Column(name="EXPIRY_DATE")
	private String expiryDate;

	private String frequency;

	@Column(name="FROM_ACCOUNT")
	private String fromAccount;

	@Column(name="FROM_DATE")
	private Date fromDate;

	private String imei;

	@Column(name="JSON_DATA")
	private String jsonData;

	private double latitude;

	private double longitude;

	@Column(name="MAX_TRIES")
	private BigDecimal maxTries;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	@Column(name="NAME_ON_CARD")
	private String nameOnCard;

	private String narration;

	@Column(name="OTP_REQUIRED")
	private String otpRequired;

	@Column(name="OTP_TYPE")
	private String otpType;

	private String otpin;

	private String pan;

	@Column(name="PURPOSE_OF_PAYMENT")
	private String purposeOfPayment;

	@Column(name="REQUEST_MONEY_ID")
	private BigDecimal requestMoneyId;

	@Column(name="SCHEDULE_TYPE")
	private String scheduleType;

	private String status;

	@Column(name="TO_ACCOUNT")
	private String toAccount;

	@Column(name="TO_DATE")
	private Date toDate;

	@Column(name="TRANS_REFNUM")
	private BigDecimal transRefnum;

	@Column(name="TYPE")
	private String type;

	private String utilitycompanyid;

	private String utilityconsumernumber;

	@Column(name="UUID")
	private String uuid;

	//bi-directional many-to-one association to TblResponse
	@OneToMany(mappedBy="tblRequest")
	private List<TblResponse> tblResponses;

	public TblRequest() {
	}

	public long getRequestId() {
		return this.requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public BigDecimal getAccountId() {
		return this.accountId;
	}

	public void setAccountId(BigDecimal accountId) {
		this.accountId = accountId;
	}

	public String getAccountImd() {
		return this.accountImd;
	}

	public void setAccountImd(String accountImd) {
		this.accountImd = accountImd;
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

	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAddBeneficiary() {
		return this.addBeneficiary;
	}

	public void setAddBeneficiary(String addBeneficiary) {
		this.addBeneficiary = addBeneficiary;
	}

	public String getAmount() {
		return this.amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAmountPaid() {
		return this.amountPaid;
	}

	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getBeneficiaryEmail() {
		return this.beneficiaryEmail;
	}

	public void setBeneficiaryEmail(String beneficiaryEmail) {
		this.beneficiaryEmail = beneficiaryEmail;
	}

	public String getBeneficiaryIban() {
		return this.beneficiaryIban;
	}

	public void setBeneficiaryIban(String beneficiaryIban) {
		this.beneficiaryIban = beneficiaryIban;
	}

	public String getBeneficiaryMobile() {
		return this.beneficiaryMobile;
	}

	public void setBeneficiaryMobile(String beneficiaryMobile) {
		this.beneficiaryMobile = beneficiaryMobile;
	}

	public String getBeneficiaryName() {
		return this.beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBranchCode() {
		return this.branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return this.branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getCardId() {
		return this.cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getCardType() {
		return this.cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public BigDecimal getChannels() {
		return this.channels;
	}

	public void setChannels(BigDecimal channels) {
		this.channels = channels;
	}

	public String getCnic() {
		return this.cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getConsentChannel() {
		return this.consentChannel;
	}

	public void setConsentChannel(String consentChannel) {
		this.consentChannel = consentChannel;
	}

	public String getConsentStatus() {
		return this.consentStatus;
	}

	public void setConsentStatus(String consentStatus) {
		this.consentStatus = consentStatus;
	}

	public String getConsentTransaction() {
		return this.consentTransaction;
	}

	public void setConsentTransaction(String consentTransaction) {
		this.consentTransaction = consentTransaction;
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

	public BigDecimal getCustomerAllId() {
		return this.customerAllId;
	}

	public void setCustomerAllId(BigDecimal customerAllId) {
		this.customerAllId = customerAllId;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDeliveryAddress() {
		return this.deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDeliveryType() {
		return this.deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public BigDecimal getDisputeType() {
		return this.disputeType;
	}

	public void setDisputeType(BigDecimal disputeType) {
		this.disputeType = disputeType;
	}

	public String getEndPoint() {
		return this.endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getFrequency() {
		return this.frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getFromAccount() {
		return this.fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public Object getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public String getImei() {
		return this.imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getJsonData() {
		return this.jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getMaxTries() {
		return this.maxTries;
	}

	public void setMaxTries(BigDecimal maxTries) {
		this.maxTries = maxTries;
	}

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getNameOnCard() {
		return this.nameOnCard;
	}

	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}

	public String getNarration() {
		return this.narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getOtpRequired() {
		return this.otpRequired;
	}

	public void setOtpRequired(String otpRequired) {
		this.otpRequired = otpRequired;
	}

	public String getOtpType() {
		return this.otpType;
	}

	public void setOtpType(String otpType) {
		this.otpType = otpType;
	}

	public String getOtpin() {
		return this.otpin;
	}

	public void setOtpin(String otpin) {
		this.otpin = otpin;
	}

	public String getPan() {
		return this.pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getPurposeOfPayment() {
		return this.purposeOfPayment;
	}

	public void setPurposeOfPayment(String purposeOfPayment) {
		this.purposeOfPayment = purposeOfPayment;
	}

	public BigDecimal getRequestMoneyId() {
		return this.requestMoneyId;
	}

	public void setRequestMoneyId(BigDecimal requestMoneyId) {
		this.requestMoneyId = requestMoneyId;
	}

	public String getScheduleType() {
		return this.scheduleType;
	}

	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getToAccount() {
		return this.toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public Object getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public BigDecimal getTransRefnum() {
		return this.transRefnum;
	}

	public void setTransRefnum(BigDecimal transRefnum) {
		this.transRefnum = transRefnum;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUtilitycompanyid() {
		return this.utilitycompanyid;
	}

	public void setUtilitycompanyid(String utilitycompanyid) {
		this.utilitycompanyid = utilitycompanyid;
	}

	public String getUtilityconsumernumber() {
		return this.utilityconsumernumber;
	}

	public void setUtilityconsumernumber(String utilityconsumernumber) {
		this.utilityconsumernumber = utilityconsumernumber;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List<TblResponse> getTblResponses() {
		return this.tblResponses;
	}

	public void setTblResponses(List<TblResponse> tblResponses) {
		this.tblResponses = tblResponses;
	}

	public TblResponse addTblRespons(TblResponse tblRespons) {
		getTblResponses().add(tblRespons);
		tblRespons.setTblRequest(this);

		return tblRespons;
	}

	public TblResponse removeTblRespons(TblResponse tblRespons) {
		getTblResponses().remove(tblRespons);
		tblRespons.setTblRequest(null);

		return tblRespons;
	}

}