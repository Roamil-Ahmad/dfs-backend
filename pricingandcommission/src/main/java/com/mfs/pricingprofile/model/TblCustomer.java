package com.mfs.pricingprofile.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
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
	@SequenceGenerator(name="TBL_CUSTOMER_CUSTOMERID_GENERATOR", sequenceName="TBL_CUSTOMER_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CUSTOMER_CUSTOMERID_GENERATOR")
	@Column(name="CUSTOMER_ID")
	private long customerId;

	@Column(name="ADDRESS_C")
	private String addressC;

	@Column(name="ADDRESS_M")
	private String addressM;

	@Column(name="ADDRESS_P")
	private String addressP;

	@Column(name="APPROVED_YN")
	private String approvedYn;

	@Column(name="BIOMETRIC_VERIFIED")
	private String biometricVerified;

	@Column(name="BUSINESS_ADDRESS")
	private String businessAddress;

	@Column(name="BUSINESS_NAME")
	private String businessName;

	@Column(name="CELL_NO_OTHER")
	private String cellNoOther;

	@Column(name="CELL_NO_PAKISTAN")
	private String cellNoPakistan;
	@Column(name="NID_NO")

	private BigDecimal cnic;

	@Temporal(TemporalType.DATE)
	@Column(name="CNIC_EXPIRY_DATE")
	private Date cnicExpiryDate;

	@Temporal(TemporalType.DATE)
	@Column(name="CNIC_ISSUE_DATE")
	private Date cnicIssueDate;

	@Column(name="CNIC_VERIFIED")
	private String cnicVerified;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Column(name="CUSTOMER_SMS")
	private String customerSms;

	@Temporal(TemporalType.DATE)
	private Date dob;

	private String email;

	@Column(name="EMPLOYER_NAME")
	private String employerName;

	@Column(name="EXISTING_RELATIONSHIP")
	private String existingRelationship;

	@Column(name="EXPECTED_MONTHLY_CREDIT")
	private BigDecimal expectedMonthlyCredit;

	@Column(name="FATHER_NAME")
	private String fatherName;

	@Column(name="FIRST_NAME")
	private String firstName;

	private String gender;

	@Column(name="GROSS_MONTHLY_SALARY")
	private BigDecimal grossMonthlySalary;

	@Column(name="INITIAL_DEPOSIT")
	private BigDecimal initialDeposit;

	@Column(name="IS_FILER")
	private String isFiler;

	@Column(name="LANDLINE_OTHER")
	private String landlineOther;

	@Column(name="LANDLINE_PAKISTAN")
	private String landlinePakistan;

	@Column(name="LAST_NAME")
	private String lastName;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="LKP_BIRTH_COUNTRY_ID")
	private BigDecimal lkpBirthCountryId;

	@Column(name="LKP_CITY_ID")
	private BigDecimal lkpCityId;

	@Column(name="LKP_COUNTRY_ID")
	private BigDecimal lkpCountryId;

	@Column(name="LKP_DISTRICT_ID")
	private BigDecimal lkpDistrictId;

	@Column(name="LKP_PROVINCE_ID")
	private BigDecimal lkpProvinceId;

	@Column(name="LKP_RESIDENCE_COUNTRY_ID")
	private BigDecimal lkpResidenceCountryId;

	@Column(name="LKP_TALUKA_ID")
	private BigDecimal lkpTalukaId;

	@Column(name="LKP_TEHSIL_ID")
	private BigDecimal lkpTehsilId;

	@Column(name="LKP_TOWN_ID")
	private BigDecimal lkpTownId;

	@Column(name="LKP_TRANS_MODE_ID")
	private BigDecimal lkpTransModeId;

	private String loginpin;

	@Column(name="MIDDLE_NAME")
	private String middleName;

	@Column(name="MODE_OF_TRANS")
	private String modeOfTrans;

	@Column(name="MOTHER_NAME")
	private String motherName;

	private String nok;

	@Column(name="NOK_MOBILE")
	private String nokMobile;

	private String pep;

	private String pob;

	@Column(name="SOURCE_OF_INCOME")
	private String sourceOfIncome;

	private String isActive;

	@Column(name="UC_NO")
	private String ucNo;

	private BigDecimal updateindex;

	@Column(name="US_RESIDENCE")
	private String usResidence;

	//bi-directional many-to-one association to TblAccount
	@OneToMany(mappedBy="tblCustomer")
	private List<TblAccount> tblAccounts;

	//bi-directional many-to-one association to TblCustomerAll
	@ManyToOne
	@JoinColumn(name="CUSTOMER_ALL_ID")
	private TblCustomerAll tblCustomerAll;

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

	public String getApprovedYn() {
		return this.approvedYn;
	}

	public void setApprovedYn(String approvedYn) {
		this.approvedYn = approvedYn;
	}

	public String getBiometricVerified() {
		return this.biometricVerified;
	}

	public void setBiometricVerified(String biometricVerified) {
		this.biometricVerified = biometricVerified;
	}

	public String getBusinessAddress() {
		return this.businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getBusinessName() {
		return this.businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getCellNoOther() {
		return this.cellNoOther;
	}

	public void setCellNoOther(String cellNoOther) {
		this.cellNoOther = cellNoOther;
	}

	public String getCellNoPakistan() {
		return this.cellNoPakistan;
	}

	public void setCellNoPakistan(String cellNoPakistan) {
		this.cellNoPakistan = cellNoPakistan;
	}

	public BigDecimal getCnic() {
		return this.cnic;
	}

	public void setCnic(BigDecimal cnic) {
		this.cnic = cnic;
	}

	public Date getCnicExpiryDate() {
		return this.cnicExpiryDate;
	}

	public void setCnicExpiryDate(Date cnicExpiryDate) {
		this.cnicExpiryDate = cnicExpiryDate;
	}

	public Date getCnicIssueDate() {
		return this.cnicIssueDate;
	}

	public void setCnicIssueDate(Date cnicIssueDate) {
		this.cnicIssueDate = cnicIssueDate;
	}

	public String getCnicVerified() {
		return this.cnicVerified;
	}

	public void setCnicVerified(String cnicVerified) {
		this.cnicVerified = cnicVerified;
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

	public String getEmployerName() {
		return this.employerName;
	}

	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	public String getExistingRelationship() {
		return this.existingRelationship;
	}

	public void setExistingRelationship(String existingRelationship) {
		this.existingRelationship = existingRelationship;
	}

	public BigDecimal getExpectedMonthlyCredit() {
		return this.expectedMonthlyCredit;
	}

	public void setExpectedMonthlyCredit(BigDecimal expectedMonthlyCredit) {
		this.expectedMonthlyCredit = expectedMonthlyCredit;
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

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public BigDecimal getGrossMonthlySalary() {
		return this.grossMonthlySalary;
	}

	public void setGrossMonthlySalary(BigDecimal grossMonthlySalary) {
		this.grossMonthlySalary = grossMonthlySalary;
	}

	public BigDecimal getInitialDeposit() {
		return this.initialDeposit;
	}

	public void setInitialDeposit(BigDecimal initialDeposit) {
		this.initialDeposit = initialDeposit;
	}

	public String getIsFiler() {
		return this.isFiler;
	}

	public void setIsFiler(String isFiler) {
		this.isFiler = isFiler;
	}

	public String getLandlineOther() {
		return this.landlineOther;
	}

	public void setLandlineOther(String landlineOther) {
		this.landlineOther = landlineOther;
	}

	public String getLandlinePakistan() {
		return this.landlinePakistan;
	}

	public void setLandlinePakistan(String landlinePakistan) {
		this.landlinePakistan = landlinePakistan;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getLastupdatedate() {
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

	public BigDecimal getLkpBirthCountryId() {
		return this.lkpBirthCountryId;
	}

	public void setLkpBirthCountryId(BigDecimal lkpBirthCountryId) {
		this.lkpBirthCountryId = lkpBirthCountryId;
	}

	public BigDecimal getLkpCityId() {
		return this.lkpCityId;
	}

	public void setLkpCityId(BigDecimal lkpCityId) {
		this.lkpCityId = lkpCityId;
	}

	public BigDecimal getLkpCountryId() {
		return this.lkpCountryId;
	}

	public void setLkpCountryId(BigDecimal lkpCountryId) {
		this.lkpCountryId = lkpCountryId;
	}

	public BigDecimal getLkpDistrictId() {
		return this.lkpDistrictId;
	}

	public void setLkpDistrictId(BigDecimal lkpDistrictId) {
		this.lkpDistrictId = lkpDistrictId;
	}

	public BigDecimal getLkpProvinceId() {
		return this.lkpProvinceId;
	}

	public void setLkpProvinceId(BigDecimal lkpProvinceId) {
		this.lkpProvinceId = lkpProvinceId;
	}

	public BigDecimal getLkpResidenceCountryId() {
		return this.lkpResidenceCountryId;
	}

	public void setLkpResidenceCountryId(BigDecimal lkpResidenceCountryId) {
		this.lkpResidenceCountryId = lkpResidenceCountryId;
	}

	public BigDecimal getLkpTalukaId() {
		return this.lkpTalukaId;
	}

	public void setLkpTalukaId(BigDecimal lkpTalukaId) {
		this.lkpTalukaId = lkpTalukaId;
	}

	public BigDecimal getLkpTehsilId() {
		return this.lkpTehsilId;
	}

	public void setLkpTehsilId(BigDecimal lkpTehsilId) {
		this.lkpTehsilId = lkpTehsilId;
	}

	public BigDecimal getLkpTownId() {
		return this.lkpTownId;
	}

	public void setLkpTownId(BigDecimal lkpTownId) {
		this.lkpTownId = lkpTownId;
	}

	public BigDecimal getLkpTransModeId() {
		return this.lkpTransModeId;
	}

	public void setLkpTransModeId(BigDecimal lkpTransModeId) {
		this.lkpTransModeId = lkpTransModeId;
	}

	public String getLoginpin() {
		return this.loginpin;
	}

	public void setLoginpin(String loginpin) {
		this.loginpin = loginpin;
	}

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getModeOfTrans() {
		return this.modeOfTrans;
	}

	public void setModeOfTrans(String modeOfTrans) {
		this.modeOfTrans = modeOfTrans;
	}

	public String getMotherName() {
		return this.motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getNok() {
		return this.nok;
	}

	public void setNok(String nok) {
		this.nok = nok;
	}

	public String getNokMobile() {
		return this.nokMobile;
	}

	public void setNokMobile(String nokMobile) {
		this.nokMobile = nokMobile;
	}

	public String getPep() {
		return this.pep;
	}

	public void setPep(String pep) {
		this.pep = pep;
	}

	public String getPob() {
		return this.pob;
	}

	public void setPob(String pob) {
		this.pob = pob;
	}

	public String getSourceOfIncome() {
		return this.sourceOfIncome;
	}

	public void setSourceOfIncome(String sourceOfIncome) {
		this.sourceOfIncome = sourceOfIncome;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getUcNo() {
		return this.ucNo;
	}

	public void setUcNo(String ucNo) {
		this.ucNo = ucNo;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public String getUsResidence() {
		return this.usResidence;
	}

	public void setUsResidence(String usResidence) {
		this.usResidence = usResidence;
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

	public TblCustomerAll getTblCustomerAll() {
		return this.tblCustomerAll;
	}

	public void setTblCustomerAll(TblCustomerAll tblCustomerAll) {
		this.tblCustomerAll = tblCustomerAll;
	}

}