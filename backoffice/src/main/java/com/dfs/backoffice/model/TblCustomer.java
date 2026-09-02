package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_CUSTOMER database table.
 * 
 */
@Entity
@Table(name="TBL_CUSTOMER")
@NamedQuery(name="TblCustomer.findAll", query="SELECT t FROM TblCustomer t")
public class TblCustomer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CUSTOMER_CUSTOMERID_GENERATOR", sequenceName="TBL_CUSTOMER_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CUSTOMER_CUSTOMERID_GENERATOR")
	@Column(name="CUSTOMER_ID")
	private long customerId;

	@Column(name="ACCOUNT_PURPOSE_ID")
	private BigDecimal accountPurposeId;

	@Column(name="ADDRESS_C")
	private String addressC;

	@Column(name="ADDRESS_M")
	private String addressM;

	@Column(name="ADDRESS_P")
	private String addressP;

	@Column(name="BIOMETRIC_VERIFIED")
	private String biometricVerified;

	@Column(name="CITY_ID")
	private BigDecimal cityId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="CUSTOMER_ALL_ID")
	private BigDecimal customerAllId;

	@Column(name="CUSTOMER_SMS")
	private String customerSms;

	@Column(name="DISTRICT_ID")
	private BigDecimal districtId;

	@Temporal(TemporalType.DATE)
	private Date dob;

	private String email;

	@Column(name="EMAIL_VERIFIED")
	private String emailVerified;

	@Column(name="FATHER_NAME")
	private String fatherName;

	@Column(name="FIRST_NAME")
	private String firstName;

	@Column(name="FULL_NAME")
	private String fullName;

	private String gender;

	@Column(name="GRANDFATHER_NAME")
	private String grandfatherName;

	@Column(name="IS_ACTIVE")
	private String isActive;

	@Column(name="IS_BLACKLISTED")
	private String isBlacklisted;

	@Column(name="IS_FILER")
	private String isFiler;

	@Column(name="IS_KYC_VERIFIED")
	private String isKycVerified;

	@Column(name="LAST_NAME")
	private String lastName;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MIDDLE_NAME")
	private String middleName;

	@Column(name="MRZ_DATA")
	private String mrzData;

	private String pob;

	@Column(name="PROVINCE_ID")
	private BigDecimal provinceId;

	@Column(name="RISK_PROFILE")
	private String riskProfile;

	@Column(name="SEGMENT_ID")
	private BigDecimal segmentId;

	@Column(name="SOURCE_OF_INCOME_OTHER")
	private String sourceOfIncome;

	@Column(name="STATUS_ID")
	private BigDecimal statusId;

	@Column(name="NID_NO")
	private String nidNo;

	@Temporal(TemporalType.DATE)
	@Column(name="NID_EXPIRY_DATE")
	private Date nidExpiryDate;

	@Temporal(TemporalType.DATE)
	@Column(name="NID_ISSUE_DATE")
	private Date nidIssueDate;

	@Column(name="NID_VERIFIED")
	private String nidVerified;

	private BigDecimal updateindex;

	private String village;

	public long getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getAccountPurposeId() {
		return this.accountPurposeId;
	}

	public void setAccountPurposeId(BigDecimal accountPurposeId) {
		this.accountPurposeId = accountPurposeId;
	}

	public String getAddressC() {
		return this.addressC;
	}

	public void setAddressC(String addressC) {
		this.addressC = addressC;
	}

	public String getAddressM() {
		return this.addressM;
	}

	public void setAddressM(String addressM) {
		this.addressM = addressM;
	}

	public String getAddressP() {
		return this.addressP;
	}

	public void setAddressP(String addressP) {
		this.addressP = addressP;
	}

	public String getBiometricVerified() {
		return this.biometricVerified;
	}

	public void setBiometricVerified(String biometricVerified) {
		this.biometricVerified = biometricVerified;
	}

	public BigDecimal getCityId() {
		return this.cityId;
	}

	public void setCityId(BigDecimal cityId) {
		this.cityId = cityId;
	}

	public Date getCreatedate() {
		return createdate;
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

	public String getCustomerSms() {
		return this.customerSms;
	}

	public void setCustomerSms(String customerSms) {
		this.customerSms = customerSms;
	}

	public BigDecimal getDistrictId() {
		return this.districtId;
	}

	public void setDistrictId(BigDecimal districtId) {
		this.districtId = districtId;
	}

	public Date getDob() {
		return this.dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailVerified() {
		return this.emailVerified;
	}

	public void setEmailVerified(String emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getFatherName() {
		return this.fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGrandfatherName() {
		return this.grandfatherName;
	}

	public void setGrandfatherName(String grandfatherName) {
		this.grandfatherName = grandfatherName;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsBlacklisted() {
		return this.isBlacklisted;
	}

	public void setIsBlacklisted(String isBlacklisted) {
		this.isBlacklisted = isBlacklisted;
	}

	public String getIsFiler() {
		return this.isFiler;
	}

	public void setIsFiler(String isFiler) {
		this.isFiler = isFiler;
	}

	public String getIsKycVerified() {
		return this.isKycVerified;
	}

	public void setIsKycVerified(String isKycVerified) {
		this.isKycVerified = isKycVerified;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getLastupdatedate() {
		return lastupdatedate;
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

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getMrzData() {
		return this.mrzData;
	}

	public void setMrzData(String mrzData) {
		this.mrzData = mrzData;
	}

	public String getPob() {
		return this.pob;
	}

	public void setPob(String pob) {
		this.pob = pob;
	}

	public BigDecimal getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(BigDecimal provinceId) {
		this.provinceId = provinceId;
	}

	public String getRiskProfile() {
		return this.riskProfile;
	}

	public void setRiskProfile(String riskProfile) {
		this.riskProfile = riskProfile;
	}

	public BigDecimal getSegmentId() {
		return this.segmentId;
	}

	public void setSegmentId(BigDecimal segmentId) {
		this.segmentId = segmentId;
	}

	public String getSourceOfIncome() {
		return this.sourceOfIncome;
	}

	public void setSourceOfIncome(String sourceOfIncome) {
		this.sourceOfIncome = sourceOfIncome;
	}

	public BigDecimal getStatusId() {
		return this.statusId;
	}

	public void setStatusId(BigDecimal statusId) {
		this.statusId = statusId;
	}

	public String getNidNo() {
		return this.nidNo;
	}

	public void setNidNo(String nidNo) {
		this.nidNo = nidNo;
	}

	public Date getNidExpiryDate() {
		return this.nidExpiryDate;
	}

	public void setNidExpiryDate(Date nidExpiryDate) {
		this.nidExpiryDate = nidExpiryDate;
	}

	public Date getNidIssueDate() {
		return this.nidIssueDate;
	}

	public void setNidIssueDate(Date nidIssueDate) {
		this.nidIssueDate = nidIssueDate;
	}

	public String getNidVerified() {
		return this.nidVerified;
	}

	public void setNidVerified(String nidVerified) {
		this.nidVerified = nidVerified;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public String getVillage() {
		return this.village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

}