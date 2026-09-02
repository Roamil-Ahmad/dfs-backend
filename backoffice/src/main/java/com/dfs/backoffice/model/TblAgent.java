package com.dfs.backoffice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
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

	@Column(name="AGENT_CLASS_ID")
	private BigDecimal agentClassId;

	@Column(name="AGENT_LEVEL")
	private BigDecimal agentLevel;

	@Column(name="AGENT_SMS")
	private String agentSms;

	@Column(name="AGENT_TYPE")
	private String agentType;

	@Column(name="CITY_ID")
	private BigDecimal cityId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="CUSTOMER_ALL_ID")
	private BigDecimal customerAllId;

	//bi-directional many-to-one association to LkpDistrict
	@ManyToOne
	@JoinColumn(name="DISTRICT_ID")
	private LkpDistrict lkpDistrict;

	//bi-directional many-to-one association to LkpProvince
	@ManyToOne
	@JoinColumn(name="PROVINCE_ID")
	private LkpProvince lkpProvince;

	@Temporal(TemporalType.DATE)
	private Date dob;

	private String email;

	@Column(name="FACEMATCH_SCORE")
	private String facematchScore;

	@Column(name="FATHER_HUSBAND_NAME")
	private String fatherHusbandName;

	private String gender;

	@Column(name="GRANDFATHER_NAME")
	private String grandfatherName;

	@Column(name="IS_ACTIVE")
	private String isActive;

	@Column(name="IS_BLACKLISTED")
	private String isBlacklisted;

	@Column(name="IS_DEVICE_REGISTERED")
	private String isDeviceRegistered;

	@Column(name="IS_FILER")
	private String isFiler;

	@Column(name="LAST_NAME")
	private String lastName;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="LIVENESS_SCORE")
	private String livenessScore;

	private String mobile;

	@Column(name="MRZ_DATA")
	private String mrzData;

	private String name;

	private String nok;

	private String occupation;

	@Column(name="PERMANENT_ADDRESS")
	private String permanentAddress;

	private String phone;

	private String pob;

	@Column(name="RESIDENTIAL_ADDRESS")
	private String residentialAddress;

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

	@Column(name="TRANSACTION_LIMIT")
	private String transactionLimit;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblAgent
	@ManyToOne
	@JoinColumn(name="PARENT_AGENT_ID")
	private TblAgent tblAgent;

	//bi-directional many-to-one association to TblAgent
    @JsonIgnore
	@OneToMany(mappedBy="tblAgent")
	private List<TblAgent> tblAgents;
	@Column(name="AGENT_CODE")
	private String agentCode;

	public TblAgent() {
	}

	public long getAgentId() {
		return this.agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	public BigDecimal getAgentClassId() {
		return this.agentClassId;
	}

	public void setAgentClassId(BigDecimal agentClassId) {
		this.agentClassId = agentClassId;
	}

	public BigDecimal getAgentLevel() {
		return this.agentLevel;
	}

	public void setAgentLevel(BigDecimal agentLevel) {
		this.agentLevel = agentLevel;
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

	public BigDecimal getCityId() {
		return this.cityId;
	}

	public void setCityId(BigDecimal cityId) {
		this.cityId = cityId;
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

	public String getFacematchScore() {
		return this.facematchScore;
	}

	public void setFacematchScore(String facematchScore) {
		this.facematchScore = facematchScore;
	}

	public String getFatherHusbandName() {
		return this.fatherHusbandName;
	}

	public void setFatherHusbandName(String fatherHusbandName) {
		this.fatherHusbandName = fatherHusbandName;
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

	public String getIsDeviceRegistered() {
		return this.isDeviceRegistered;
	}

	public void setIsDeviceRegistered(String isDeviceRegistered) {
		this.isDeviceRegistered = isDeviceRegistered;
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

	public String getLivenessScore() {
		return this.livenessScore;
	}

	public void setLivenessScore(String livenessScore) {
		this.livenessScore = livenessScore;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMrzData() {
		return this.mrzData;
	}

	public void setMrzData(String mrzData) {
		this.mrzData = mrzData;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPermanentAddress() {
		return this.permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPob() {
		return this.pob;
	}

	public void setPob(String pob) {
		this.pob = pob;
	}

	public String getResidentialAddress() {
		return this.residentialAddress;
	}

	public void setResidentialAddress(String residentialAddress) {
		this.residentialAddress = residentialAddress;
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

	public String getTransactionLimit() {
		return this.transactionLimit;
	}

	public void setTransactionLimit(String transactionLimit) {
		this.transactionLimit = transactionLimit;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public TblAgent getTblAgent() {
		return this.tblAgent;
	}

	public void setTblAgent(TblAgent tblAgent) {
		this.tblAgent = tblAgent;
	}

	public List<TblAgent> getTblAgents() {
		return this.tblAgents;
	}

	public void setTblAgents(List<TblAgent> tblAgents) {
		this.tblAgents = tblAgents;
	}

	public TblAgent addTblAgent(TblAgent tblAgent) {
		getTblAgents().add(tblAgent);
		tblAgent.setTblAgent(this);

		return tblAgent;
	}

	public TblAgent removeTblAgent(TblAgent tblAgent) {
		getTblAgents().remove(tblAgent);
		tblAgent.setTblAgent(null);

		return tblAgent;
	}
	public LkpDistrict getLkpDistrict() {
		return lkpDistrict;
	}

	public void setLkpDistrict(LkpDistrict lkpDistrict) {
		this.lkpDistrict = lkpDistrict;
	}

	public LkpProvince getLkpProvince() {
		return lkpProvince;
	}

	public void setLkpProvince(LkpProvince lkpProvince) {
		this.lkpProvince = lkpProvince;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
}