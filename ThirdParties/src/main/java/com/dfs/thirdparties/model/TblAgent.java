package com.dfs.thirdparties.model;

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
	@SequenceGenerator(name="TBL_AGENT_AGENTID_GENERATOR", sequenceName="TBL_AGENT_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_AGENT_AGENTID_GENERATOR")
	@Column(name="AGENT_ID")
	private long agentId;

	@Column(name="AGENT_LEVEL")
	private BigDecimal agentLevel;

	@Column(name="AGENT_SMS")
	private String agentSms;

	@Column(name="AGENT_TYPE")
	private String agentType;

	private Date createdate;

	private BigDecimal createuser;

	private String email;

	@Column(name="FATHER_HUSBAND_NAME")
	private String fatherHusbandName;

	@Column(name="GRANDFATHER_NAME")
	private String grandfatherName;

	@Column(name="IS_ACTIVE")
	private String isActive;

	@Column(name="IS_FILER")
	private String isFiler;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private String mobile;

	private String name;

	private String nok;

	private String occupation;

	private String phone;

	private String pob;

	@Column(name="RESIDENTIAL_ADDRESS")
	private String residentialAddress;

	@Column(name="NID_NO")
	private String nidNo;

	@Column(name="NID_EXPIRY_DATE")
	private Date nidExpiryDate;

	@Column(name="NID_ISSUE_DATE")
	private Date nidIssueDate;

	@Column(name="TRANSACTION_LIMIT")
	private String transactionLimit;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblAccount
	@OneToMany(mappedBy="tblAgent")
	private List<TblAccount> tblAccounts;

	//bi-directional many-to-one association to LkpCity
	@ManyToOne
	@JoinColumn(name="CITY_ID")
	private LkpCity lkpCity;

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

	//bi-directional many-to-one association to TblAgent
	@ManyToOne
	@JoinColumn(name="PARENT_AGENT_ID")
	private TblAgent tblAgent1;

	//bi-directional many-to-one association to TblAgent
	@OneToMany(mappedBy="tblAgent1")
	private List<TblAgent> tblAgents1;

	//bi-directional many-to-one association to TblAgentClass
	@ManyToOne
	@JoinColumn(name="AGENT_CLASS_ID")
	private TblAgentClass tblAgentClass;

	//bi-directional many-to-one association to TblAgentCommissionDistribution
	@OneToMany(mappedBy="tblAgent")
	private List<TblAgentCommissionDistribution> tblAgentCommissionDistributions;

	public TblAgent() {
	}

	public long getAgentId() {
		return this.agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
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

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public TblAgentClass getTblAgentClass() {
		return this.tblAgentClass;
	}

	public void setTblAgentClass(TblAgentClass tblAgentClass) {
		this.tblAgentClass = tblAgentClass;
	}

	public List<TblAgentCommissionDistribution> getTblAgentCommissionDistributions() {
		return this.tblAgentCommissionDistributions;
	}

	public void setTblAgentCommissionDistributions(List<TblAgentCommissionDistribution> tblAgentCommissionDistributions) {
		this.tblAgentCommissionDistributions = tblAgentCommissionDistributions;
	}

	public TblAgentCommissionDistribution addTblAgentCommissionDistribution(TblAgentCommissionDistribution tblAgentCommissionDistribution) {
		getTblAgentCommissionDistributions().add(tblAgentCommissionDistribution);
		tblAgentCommissionDistribution.setTblAgent(this);

		return tblAgentCommissionDistribution;
	}

	public TblAgentCommissionDistribution removeTblAgentCommissionDistribution(TblAgentCommissionDistribution tblAgentCommissionDistribution) {
		getTblAgentCommissionDistributions().remove(tblAgentCommissionDistribution);
		tblAgentCommissionDistribution.setTblAgent(null);

		return tblAgentCommissionDistribution;
	}

}