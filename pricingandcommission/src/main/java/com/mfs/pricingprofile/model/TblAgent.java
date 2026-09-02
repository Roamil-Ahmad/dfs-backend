package com.mfs.pricingprofile.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the TBL_AGENT database table.
 * 
 */
@Entity
@Table(name="TBL_AGENT")
@NamedQuery(name="TblAgent.findAll", query="SELECT t FROM TblAgent t")
public class TblAgent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_AGENT_AGENTID_GENERATOR", sequenceName="TBL_AGENT_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_AGENT_AGENTID_GENERATOR")
	@Column(name="AGENT_ID")
	private long agentId;

	private String address;

	@Column(name="AGENT_SMS")
	private String agentSms;

	@Column(name="AGENT_TYPE")
	private String agentType;

	@Column(name="STATUS_ID")
	private BigDecimal statusId;
	@Column(name="NID_NO")

	private BigDecimal cnic;

	@Column(name="CNIC_APPLICANT")
	private BigDecimal cnicApplicant;

	@Temporal(TemporalType.DATE)
	@Column(name="CNIC_EXPIRY_DATE")
	private Date cnicExpiryDate;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	private String email;

	@Column(name="FATHER_HUSBAND_NAME")
	private String fatherHusbandName;

	@Column(name="IS_FILER")
	private String isFiler;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="LKP_CIRCLE_ID")
	private BigDecimal lkpCircleId;

	@Column(name="LKP_CITY_ID")
	private BigDecimal lkpCityId;

	@Column(name="LKP_DISTRICT_ID")
	private BigDecimal lkpDistrictId;

	@Column(name="LKP_GPO_ID")
	private BigDecimal lkpGpoId;

	@Column(name="LKP_POST_OFFICE_ID")
	private BigDecimal lkpPostOfficeId;

	@Column(name="LKP_PROVINCE_ID")
	private BigDecimal lkpProvinceId;

	private String mobile;

	@Column(name="MOBILE_APPLICANT")
	private String mobileApplicant;

	@Column(name="MOTHER_NAME")
	private String motherName;

	private String mpin;

	private String name;

	@Column(name="NAME_OF_APPLICANT")
	private String nameOfApplicant;

	private String nok;

	private String occupation;

	private String password;

	private String phone;

	@Column(name="PHONE_APPLICANT")
	private String phoneApplicant;

	private String pob;

	@Column(name="PWD_UPDATE_FLAG")
	private String pwdUpdateFlag;

	@Column(name="RELEVANT_GPO")
	private String relevantGpo;

	@Column(name="RESIDENTIAL_ADDRESS")
	private String residentialAddress;

	@Column(name="ROLE_ID")
	private BigDecimal roleId;

	private String isActive;

	private String tehsil;

	@Column(name="TRANSACTION_LIMIT")
	private String transactionLimit;

	@Column(name="UNION_COUNCIL")
	private String unionCouncil;

	private BigDecimal updateindex;

	private String username;

	//bi-directional many-to-one association to TblAccount
	@OneToMany(mappedBy="tblAgent")
	private List<TblAccount> tblAccounts;

	//bi-directional many-to-one association to TblAgent
	@ManyToOne
	@JoinColumn(name="SUB_AGENT_OF")
	private TblAgent tblAgent1;

	//bi-directional many-to-one association to TblAgent
	@OneToMany(mappedBy="tblAgent1")
	private List<TblAgent> tblAgents1;

	//bi-directional many-to-one association to TblAgent
	@ManyToOne
	@JoinColumn(name="PARENT_AGENT_ID")
	private TblAgent tblAgent2;

	//bi-directional many-to-one association to TblAgent
	@OneToMany(mappedBy="tblAgent2")
	private List<TblAgent> tblAgents2;

	//bi-directional many-to-one association to TblAgentClass
	@ManyToOne
	@JoinColumn(name="AGENT_CLASS_ID")
	private TblAgentClass tblAgentClass;

	public TblAgent() {
	}

	public long getAgentId() {
		return this.agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAgentSms() {
		return this.agentSms;
	}

	public void setAgentSms(String agentSms) {
		this.agentSms = agentSms;
	}

	public String getAgentType() {
		return this.agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public BigDecimal getStatusId() {
		return statusId;
	}

	public void setStatusId(BigDecimal statusId) {
		this.statusId = statusId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getCnic() {
		return this.cnic;
	}

	public void setCnic(BigDecimal cnic) {
		this.cnic = cnic;
	}

	public BigDecimal getCnicApplicant() {
		return this.cnicApplicant;
	}

	public void setCnicApplicant(BigDecimal cnicApplicant) {
		this.cnicApplicant = cnicApplicant;
	}

	public Date getCnicExpiryDate() {
		return this.cnicExpiryDate;
	}

	public void setCnicExpiryDate(Date cnicExpiryDate) {
		this.cnicExpiryDate = cnicExpiryDate;
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFatherHusbandName() {
		return this.fatherHusbandName;
	}

	public void setFatherHusbandName(String fatherHusbandName) {
		this.fatherHusbandName = fatherHusbandName;
	}

	public String getIsFiler() {
		return this.isFiler;
	}

	public void setIsFiler(String isFiler) {
		this.isFiler = isFiler;
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

	public BigDecimal getLkpCircleId() {
		return this.lkpCircleId;
	}

	public void setLkpCircleId(BigDecimal lkpCircleId) {
		this.lkpCircleId = lkpCircleId;
	}

	public BigDecimal getLkpCityId() {
		return this.lkpCityId;
	}

	public void setLkpCityId(BigDecimal lkpCityId) {
		this.lkpCityId = lkpCityId;
	}

	public BigDecimal getLkpDistrictId() {
		return this.lkpDistrictId;
	}

	public void setLkpDistrictId(BigDecimal lkpDistrictId) {
		this.lkpDistrictId = lkpDistrictId;
	}

	public BigDecimal getLkpGpoId() {
		return this.lkpGpoId;
	}

	public void setLkpGpoId(BigDecimal lkpGpoId) {
		this.lkpGpoId = lkpGpoId;
	}

	public BigDecimal getLkpPostOfficeId() {
		return this.lkpPostOfficeId;
	}

	public void setLkpPostOfficeId(BigDecimal lkpPostOfficeId) {
		this.lkpPostOfficeId = lkpPostOfficeId;
	}

	public BigDecimal getLkpProvinceId() {
		return this.lkpProvinceId;
	}

	public void setLkpProvinceId(BigDecimal lkpProvinceId) {
		this.lkpProvinceId = lkpProvinceId;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobileApplicant() {
		return this.mobileApplicant;
	}

	public void setMobileApplicant(String mobileApplicant) {
		this.mobileApplicant = mobileApplicant;
	}

	public String getMotherName() {
		return this.motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getMpin() {
		return this.mpin;
	}

	public void setMpin(String mpin) {
		this.mpin = mpin;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameOfApplicant() {
		return nameOfApplicant;
	}

	public void setNameOfApplicant(String nameOfApplicant) {
		this.nameOfApplicant = nameOfApplicant;
	}

	public String getNok() {
		return this.nok;
	}

	public void setNok(String nok) {
		this.nok = nok;
	}

	public String getOccupation() {
		return this.occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoneApplicant() {
		return this.phoneApplicant;
	}

	public void setPhoneApplicant(String phoneApplicant) {
		this.phoneApplicant = phoneApplicant;
	}

	public String getPob() {
		return this.pob;
	}

	public void setPob(String pob) {
		this.pob = pob;
	}

	public String getPwdUpdateFlag() {
		return this.pwdUpdateFlag;
	}

	public void setPwdUpdateFlag(String pwdUpdateFlag) {
		this.pwdUpdateFlag = pwdUpdateFlag;
	}

	public String getRelevantGpo() {
		return this.relevantGpo;
	}

	public void setRelevantGpo(String relevantGpo) {
		this.relevantGpo = relevantGpo;
	}

	public String getResidentialAddress() {
		return this.residentialAddress;
	}

	public void setResidentialAddress(String residentialAddress) {
		this.residentialAddress = residentialAddress;
	}

	public BigDecimal getRoleId() {
		return this.roleId;
	}

	public void setRoleId(BigDecimal roleId) {
		this.roleId = roleId;
	}

	public String getTehsil() {
		return this.tehsil;
	}

	public void setTehsil(String tehsil) {
		this.tehsil = tehsil;
	}

	public String getTransactionLimit() {
		return this.transactionLimit;
	}

	public void setTransactionLimit(String transactionLimit) {
		this.transactionLimit = transactionLimit;
	}

	public String getUnionCouncil() {
		return this.unionCouncil;
	}

	public void setUnionCouncil(String unionCouncil) {
		this.unionCouncil = unionCouncil;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<TblAccount> getTblAccounts() {
		return this.tblAccounts;
	}

	public void setTblAccounts(List<TblAccount> tblAccounts) {
		this.tblAccounts = tblAccounts;
	}

	public TblAccount addTblAccount(TblAccount tblAccount) {
		getTblAccounts().add(tblAccount);
		tblAccount.setTblAgent(this);

		return tblAccount;
	}

	public TblAccount removeTblAccount(TblAccount tblAccount) {
		getTblAccounts().remove(tblAccount);
		tblAccount.setTblAgent(null);

		return tblAccount;
	}

	public TblAgent getTblAgent1() {
		return this.tblAgent1;
	}

	public void setTblAgent1(TblAgent tblAgent1) {
		this.tblAgent1 = tblAgent1;
	}

	public List<TblAgent> getTblAgents1() {
		return this.tblAgents1;
	}

	public void setTblAgents1(List<TblAgent> tblAgents1) {
		this.tblAgents1 = tblAgents1;
	}

	public TblAgent addTblAgents1(TblAgent tblAgents1) {
		getTblAgents1().add(tblAgents1);
		tblAgents1.setTblAgent1(this);

		return tblAgents1;
	}

	public TblAgent removeTblAgents1(TblAgent tblAgents1) {
		getTblAgents1().remove(tblAgents1);
		tblAgents1.setTblAgent1(null);

		return tblAgents1;
	}

	public TblAgent getTblAgent2() {
		return this.tblAgent2;
	}

	public void setTblAgent2(TblAgent tblAgent2) {
		this.tblAgent2 = tblAgent2;
	}

	public List<TblAgent> getTblAgents2() {
		return this.tblAgents2;
	}

	public void setTblAgents2(List<TblAgent> tblAgents2) {
		this.tblAgents2 = tblAgents2;
	}

	public TblAgent addTblAgents2(TblAgent tblAgents2) {
		getTblAgents2().add(tblAgents2);
		tblAgents2.setTblAgent2(this);

		return tblAgents2;
	}

	public TblAgent removeTblAgents2(TblAgent tblAgents2) {
		getTblAgents2().remove(tblAgents2);
		tblAgents2.setTblAgent2(null);

		return tblAgents2;
	}

	public TblAgentClass getTblAgentClass() {
		return this.tblAgentClass;
	}

	public void setTblAgentClass(TblAgentClass tblAgentClass) {
		this.tblAgentClass = tblAgentClass;
	}

}