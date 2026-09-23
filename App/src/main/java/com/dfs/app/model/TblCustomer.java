package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;

import java.util.List;


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
	@SequenceGenerator(name="TBL_CUSTOMER_CUSTOMERID_GENERATOR", sequenceName="TBL_CUSTOMER_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CUSTOMER_CUSTOMERID_GENERATOR")
	@Column(name="CUSTOMER_ID")
	private long customerId;

	@Column(name="ADDRESS_C")
	private String addressC;

	@Column(name="ADDRESS_M")
	private String addressM;

	@Column(name="ADDRESS_P")
	private String addressP;

	@Column(name="BIOMETRIC_VERIFIED")
	private String biometricVerified;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="CUSTOMER_SMS")
	private String customerSms;

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

	@Column(name="IS_FILER")
	private String isFiler;

	@Column(name="LAST_NAME")
	private String lastName;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MIDDLE_NAME")
	private String middleName;

	@Column(name="MRZ_DATA")
	private String mrzData;

	@Column(name="IS_BLACKLISTED")
	private String isBlackListed;

	private String pob;

	@Column(name="SOURCE_OF_INCOME_OTHER")
	private String sourceOfIncomeOther;

	@Column(name="NID_NO")
	private String nidNo;

	@Column(name="NID_EXPIRY_DATE")
	private Date nidExpiryDate;

	@Column(name="NID_ISSUE_DATE")
	private Date nidIssueDate;

	@Column(name="NID_VERIFIED")
	private String nidVerified;

	@Column(name="LIVENESS_SCORE")
	private String livenessScore;

	@Column(name="FACEMATCH_SCORE")
	private String faceMatchScore;
	private BigDecimal updateindex;

	private String village;

	//bi-directional many-to-one association to TblAccount
	@OneToMany(mappedBy="tblCustomer")
	private List<TblAccount> tblAccounts;

	//bi-directional many-to-one association to TblBioverisy
	@OneToMany(mappedBy="tblCustomer")
	private List<TblBioverisy> tblBioverisys;

	//bi-directional many-to-one association to LkpAccountPurpose
	@ManyToOne
	@JoinColumn(name="ACCOUNT_PURPOSE_ID")
	private LkpAccountPurpose lkpAccountPurpose;

	//bi-directional many-to-one association to LkpCity
	@ManyToOne
	@JoinColumn(name="CITY_ID")
	private LkpCity lkpCity;

	/**
	 * The customer's segment, for reading only.
	 *
	 * <p>insertable and updatable are false deliberately: SEGMENT_ID is NOT NULL in the database and
	 * nothing in this service sets it, so letting Hibernate write the column would make it insert a
	 * null and fail every customer that is created here. The column was previously unmapped, which
	 * had the same effect of leaving it to the database - this keeps that, and adds the read.</p>
	 */
	@ManyToOne
	@JoinColumn(name="SEGMENT_ID", insertable = false, updatable = false)
	private LkpSegment lkpSegment;

	//bi-directional many-to-one association to LkpDistrict
	@ManyToOne
	@JoinColumn(name="DISTRICT_ID")
	private LkpDistrict lkpDistrict;

	//bi-directional many-to-one association to LkpProvince
	@ManyToOne
	@JoinColumn(name="PROVINCE_ID")
	private LkpProvince lkpProvince;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblCustomerAll
	@ManyToOne
	@JoinColumn(name="CUSTOMER_ALL_ID")
	private TblCustomerAll tblCustomerAll;

	//bi-directional many-to-one association to TblBeneficiary
	@OneToMany(mappedBy="tblCustomer")
	private List<TblBeneficiary> tblBeneficiaries;

	@Column(name="CUSTOMER_UID")
	private String customerUid;

	@Column(name="RISK_PROFILE")
	private String riskProfile;

	@Column(name="NATIONALITY")
	private String nationality;

	@Column(name="COUNTRY")
	private String country;

	@Column(name="OCCUPATION_OTHER")
	private String occupationOther;

	@Column(name="SIGNATURE")
	private String signature;

	@ManyToOne
	@JoinColumn(name="OCCUPATION_ID")
	private LkpOccupation lkpOccupation;

	@ManyToOne
	@JoinColumn(name="SOURCE_OF_INCOME_ID")
	private LkpSourceOfIncome lkpSourceOfIncome;

	public TblCustomer() {
	}

	public long getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
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

	public String getCustomerSms() {
		return this.customerSms;
	}

	public void setCustomerSms(String customerSms) {
		this.customerSms = customerSms;
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

	public String getIsFiler() {
		return this.isFiler;
	}

	public void setIsFiler(String isFiler) {
		this.isFiler = isFiler;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getSourceOfIncomeOther() {
		return sourceOfIncomeOther;
	}

	public void setSourceOfIncomeOther(String sourceOfIncomeOther) {
		this.sourceOfIncomeOther = sourceOfIncomeOther;
	}

	public String getNidNo() {
		return this.nidNo;
	}

	public void setNidNo(String nidNo) {
		this.nidNo = nidNo;
	}

	public Object getNidExpiryDate() {
		return this.nidExpiryDate;
	}

	public void setNidExpiryDate(Date nidExpiryDate) {
		this.nidExpiryDate = nidExpiryDate;
	}

	public Object getNidIssueDate() {
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

	public List<TblAccount> getTblAccounts() {
		return this.tblAccounts;
	}

	public void setTblAccounts(List<TblAccount> tblAccounts) {
		this.tblAccounts = tblAccounts;
	}

	public TblAccount addTblAccount(TblAccount tblAccount) {
		getTblAccounts().add(tblAccount);
		tblAccount.setTblCustomer(this);

		return tblAccount;
	}

	public TblAccount removeTblAccount(TblAccount tblAccount) {
		getTblAccounts().remove(tblAccount);
		tblAccount.setTblCustomer(null);

		return tblAccount;
	}

	public List<TblBioverisy> getTblBioverisys() {
		return this.tblBioverisys;
	}

	public void setTblBioverisys(List<TblBioverisy> tblBioverisys) {
		this.tblBioverisys = tblBioverisys;
	}

	public TblBioverisy addTblBioverisy(TblBioverisy tblBioverisy) {
		getTblBioverisys().add(tblBioverisy);
		tblBioverisy.setTblCustomer(this);

		return tblBioverisy;
	}

	public TblBioverisy removeTblBioverisy(TblBioverisy tblBioverisy) {
		getTblBioverisys().remove(tblBioverisy);
		tblBioverisy.setTblCustomer(null);

		return tblBioverisy;
	}

	public LkpAccountPurpose getLkpAccountPurpose() {
		return this.lkpAccountPurpose;
	}

	public void setLkpAccountPurpose(LkpAccountPurpose lkpAccountPurpose) {
		this.lkpAccountPurpose = lkpAccountPurpose;
	}

	public LkpSegment getLkpSegment() {
		return this.lkpSegment;
	}

	public void setLkpSegment(LkpSegment lkpSegment) {
		this.lkpSegment = lkpSegment;
	}

	public LkpCity getLkpCity() {
		return this.lkpCity;
	}

	public void setLkpCity(LkpCity lkpCity) {
		this.lkpCity = lkpCity;
	}

	public LkpDistrict getLkpDistrict() {
		return this.lkpDistrict;
	}

	public void setLkpDistrict(LkpDistrict lkpDistrict) {
		this.lkpDistrict = lkpDistrict;
	}

	public LkpProvince getLkpProvince() {
		return this.lkpProvince;
	}

	public void setLkpProvince(LkpProvince lkpProvince) {
		this.lkpProvince = lkpProvince;
	}

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

	public TblCustomerAll getTblCustomerAll() {
		return this.tblCustomerAll;
	}

	public void setTblCustomerAll(TblCustomerAll tblCustomerAll) {
		this.tblCustomerAll = tblCustomerAll;
	}

	public List<TblBeneficiary> getTblBeneficiaries() {
		return this.tblBeneficiaries;
	}

	public void setTblBeneficiaries(List<TblBeneficiary> tblBeneficiaries) {
		this.tblBeneficiaries = tblBeneficiaries;
	}

	public TblBeneficiary addTblBeneficiary(TblBeneficiary tblBeneficiary) {
		getTblBeneficiaries().add(tblBeneficiary);
		tblBeneficiary.setTblCustomer(this);

		return tblBeneficiary;
	}

	public TblBeneficiary removeTblBeneficiary(TblBeneficiary tblBeneficiary) {
		getTblBeneficiaries().remove(tblBeneficiary);
		tblBeneficiary.setTblCustomer(null);

		return tblBeneficiary;
	}

	public String getLivenessScore() {
		return livenessScore;
	}

	public void setLivenessScore(String livenessScore) {
		this.livenessScore = livenessScore;
	}

	public String getFaceMatchScore() {
		return faceMatchScore;
	}

	public void setFaceMatchScore(String faceMatchScore) {
		this.faceMatchScore = faceMatchScore;
	}

	public String getIsBlackListed() {
		return isBlackListed;
	}

	public void setIsBlackListed(String isBlackListed) {
		this.isBlackListed = isBlackListed;
	}

	public String getCustomerUid() {
		return customerUid;
	}

	public void setCustomerUid(String customerUid) {
		this.customerUid = customerUid;
	}

	public String getRiskProfile() {
		return riskProfile;
	}

	public void setRiskProfile(String riskProfile) {
		this.riskProfile = riskProfile;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOccupationOther() {
		return occupationOther;
	}

	public void setOccupationOther(String occupationOther) {
		this.occupationOther = occupationOther;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public LkpOccupation getLkpOccupation() {
		return lkpOccupation;
	}

	public void setLkpOccupation(LkpOccupation lkpOccupation) {
		this.lkpOccupation = lkpOccupation;
	}

	public LkpSourceOfIncome getLkpSourceOfIncome() {
		return lkpSourceOfIncome;
	}

	public void setLkpSourceOfIncome(LkpSourceOfIncome lkpSourceOfIncome) {
		this.lkpSourceOfIncome = lkpSourceOfIncome;
	}
}