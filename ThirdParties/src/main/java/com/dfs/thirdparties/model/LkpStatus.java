package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the LKP_STATUS database table.
 * 
 */
@Entity
@Table(name="LKP_STATUS")
@NamedQuery(name="LkpStatus.findAll", query="SELECT l FROM LkpStatus l")
public class LkpStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_STATUS_STATUSID_GENERATOR", sequenceName="LKP_STATUS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_STATUS_STATUSID_GENERATOR")
	@Column(name="STATUS_ID")
	private long statusId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="STATUS_CODE")
	private String statusCode;

	@Column(name="STATUS_DESCR")
	private String statusDescr;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpAccountPurpose
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpAccountPurpose> lkpAccountPurposes;

	//bi-directional many-to-one association to LkpAccountStatus
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpAccountStatus> lkpAccountStatuses;

	//bi-directional many-to-one association to LkpAccountType
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpAccountType> lkpAccountTypes;

	//bi-directional many-to-one association to LkpBranch
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpBranch> lkpBranches;

	//bi-directional many-to-one association to LkpChannel
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpChannel> lkpChannels;

	//bi-directional many-to-one association to LkpCharge
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpCharge> lkpCharges;

	//bi-directional many-to-one association to LkpCity
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpCity> lkpCities;

	//bi-directional many-to-one association to LkpCountry
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpCountry> lkpCountries;

	//bi-directional many-to-one association to LkpDebitCardType
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpDebitCardType> lkpDebitCardTypes;

	//bi-directional many-to-one association to LkpDisputeType
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpDisputeType> lkpDisputeTypes;

	//bi-directional many-to-one association to LkpDistrict
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpDistrict> lkpDistricts;

	//bi-directional many-to-one association to LkpLanguage
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpLanguage> lkpLanguages;

	//bi-directional many-to-one association to LkpMessageType
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpMessageType> lkpMessageTypes;

	//bi-directional many-to-one association to LkpRegion
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpRegion> lkpRegions;

	//bi-directional many-to-one association to LkpSegment
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpSegment> lkpSegments;

	//bi-directional many-to-one association to LkpTaxType
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpTaxType> lkpTaxTypes;

	//bi-directional many-to-one association to TblAgent
	@OneToMany(mappedBy="lkpStatus")
	private List<TblAgent> tblAgents;

	//bi-directional many-to-one association to TblAgentClass
	@OneToMany(mappedBy="lkpStatus")
	private List<TblAgentClass> tblAgentClasses;

	//bi-directional many-to-one association to TblCommissionProfile
	@OneToMany(mappedBy="lkpStatus")
	private List<TblCommissionProfile> tblCommissionProfiles;

	//bi-directional many-to-one association to TblCustomer
	@OneToMany(mappedBy="lkpStatus")
	private List<TblCustomer> tblCustomers;

	//bi-directional many-to-one association to TblMcConfig
	@OneToMany(mappedBy="lkpStatus")
	private List<TblMcConfig> tblMcConfigs;

	//bi-directional many-to-one association to TblMcPendingRequest
	@OneToMany(mappedBy="lkpStatus")
	private List<TblMcPendingRequest> tblMcPendingRequests;

	//bi-directional many-to-one association to TblMcRequest
	@OneToMany(mappedBy="lkpStatus")
	private List<TblMcRequest> tblMcRequests;

	//bi-directional many-to-one association to TblMcRequestAction
	@OneToMany(mappedBy="lkpStatus")
	private List<TblMcRequestAction> tblMcRequestActions;

	//bi-directional many-to-one association to TblMenu
	@OneToMany(mappedBy="lkpStatus")
	private List<TblMenu> tblMenus;

	//bi-directional many-to-one association to TblMessage
	@OneToMany(mappedBy="lkpStatus")
	private List<TblMessage> tblMessages;

	//bi-directional many-to-one association to TblRole
	@OneToMany(mappedBy="lkpStatus")
	private List<TblRole> tblRoles;

	//bi-directional many-to-one association to TblRoleRight
	@OneToMany(mappedBy="lkpStatus")
	private List<TblRoleRight> tblRoleRights;

	//bi-directional many-to-one association to TblTaxRegime
	@OneToMany(mappedBy="lkpStatus")
	private List<TblTaxRegime> tblTaxRegimes;

	//bi-directional many-to-one association to TblTransCharge
	@OneToMany(mappedBy="lkpStatus")
	private List<TblTransCharge> tblTransCharges;

	//bi-directional many-to-one association to TblTransDoc
	@OneToMany(mappedBy="lkpStatus")
	private List<TblTransDoc> tblTransDocs;

	//bi-directional many-to-one association to TblTransLimit
	@OneToMany(mappedBy="lkpStatus")
	private List<TblTransLimit> tblTransLimits;

	//bi-directional many-to-one association to TblUser
	@OneToMany(mappedBy="lkpStatus")
	private List<TblUser> tblUsers;

	public LkpStatus() {
	}

	public long getStatusId() {
		return this.statusId;
	}

	public void setStatusId(long statusId) {
		this.statusId = statusId;
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

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
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

	public String getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDescr() {
		return this.statusDescr;
	}

	public void setStatusDescr(String statusDescr) {
		this.statusDescr = statusDescr;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public List<LkpAccountPurpose> getLkpAccountPurposes() {
		return this.lkpAccountPurposes;
	}

	public void setLkpAccountPurposes(List<LkpAccountPurpose> lkpAccountPurposes) {
		this.lkpAccountPurposes = lkpAccountPurposes;
	}

	public LkpAccountPurpose addLkpAccountPurpos(LkpAccountPurpose lkpAccountPurpos) {
		getLkpAccountPurposes().add(lkpAccountPurpos);
		lkpAccountPurpos.setLkpStatus(this);

		return lkpAccountPurpos;
	}

	public LkpAccountPurpose removeLkpAccountPurpos(LkpAccountPurpose lkpAccountPurpos) {
		getLkpAccountPurposes().remove(lkpAccountPurpos);
		lkpAccountPurpos.setLkpStatus(null);

		return lkpAccountPurpos;
	}

	public List<LkpAccountStatus> getLkpAccountStatuses() {
		return this.lkpAccountStatuses;
	}

	public void setLkpAccountStatuses(List<LkpAccountStatus> lkpAccountStatuses) {
		this.lkpAccountStatuses = lkpAccountStatuses;
	}

	public LkpAccountStatus addLkpAccountStatus(LkpAccountStatus lkpAccountStatus) {
		getLkpAccountStatuses().add(lkpAccountStatus);
		lkpAccountStatus.setLkpStatus(this);

		return lkpAccountStatus;
	}

	public LkpAccountStatus removeLkpAccountStatus(LkpAccountStatus lkpAccountStatus) {
		getLkpAccountStatuses().remove(lkpAccountStatus);
		lkpAccountStatus.setLkpStatus(null);

		return lkpAccountStatus;
	}

	public List<LkpAccountType> getLkpAccountTypes() {
		return this.lkpAccountTypes;
	}

	public void setLkpAccountTypes(List<LkpAccountType> lkpAccountTypes) {
		this.lkpAccountTypes = lkpAccountTypes;
	}

	public LkpAccountType addLkpAccountType(LkpAccountType lkpAccountType) {
		getLkpAccountTypes().add(lkpAccountType);
		lkpAccountType.setLkpStatus(this);

		return lkpAccountType;
	}

	public LkpAccountType removeLkpAccountType(LkpAccountType lkpAccountType) {
		getLkpAccountTypes().remove(lkpAccountType);
		lkpAccountType.setLkpStatus(null);

		return lkpAccountType;
	}

	public List<LkpBranch> getLkpBranches() {
		return this.lkpBranches;
	}

	public void setLkpBranches(List<LkpBranch> lkpBranches) {
		this.lkpBranches = lkpBranches;
	}

	public LkpBranch addLkpBranch(LkpBranch lkpBranch) {
		getLkpBranches().add(lkpBranch);
		lkpBranch.setLkpStatus(this);

		return lkpBranch;
	}

	public LkpBranch removeLkpBranch(LkpBranch lkpBranch) {
		getLkpBranches().remove(lkpBranch);
		lkpBranch.setLkpStatus(null);

		return lkpBranch;
	}

	public List<LkpChannel> getLkpChannels() {
		return this.lkpChannels;
	}

	public void setLkpChannels(List<LkpChannel> lkpChannels) {
		this.lkpChannels = lkpChannels;
	}

	public LkpChannel addLkpChannel(LkpChannel lkpChannel) {
		getLkpChannels().add(lkpChannel);
		lkpChannel.setLkpStatus(this);

		return lkpChannel;
	}

	public LkpChannel removeLkpChannel(LkpChannel lkpChannel) {
		getLkpChannels().remove(lkpChannel);
		lkpChannel.setLkpStatus(null);

		return lkpChannel;
	}

	public List<LkpCharge> getLkpCharges() {
		return this.lkpCharges;
	}

	public void setLkpCharges(List<LkpCharge> lkpCharges) {
		this.lkpCharges = lkpCharges;
	}

	public LkpCharge addLkpCharge(LkpCharge lkpCharge) {
		getLkpCharges().add(lkpCharge);
		lkpCharge.setLkpStatus(this);

		return lkpCharge;
	}

	public LkpCharge removeLkpCharge(LkpCharge lkpCharge) {
		getLkpCharges().remove(lkpCharge);
		lkpCharge.setLkpStatus(null);

		return lkpCharge;
	}

	public List<LkpCity> getLkpCities() {
		return this.lkpCities;
	}

	public void setLkpCities(List<LkpCity> lkpCities) {
		this.lkpCities = lkpCities;
	}

	public LkpCity addLkpCity(LkpCity lkpCity) {
		getLkpCities().add(lkpCity);
		lkpCity.setLkpStatus(this);

		return lkpCity;
	}

	public LkpCity removeLkpCity(LkpCity lkpCity) {
		getLkpCities().remove(lkpCity);
		lkpCity.setLkpStatus(null);

		return lkpCity;
	}

	public List<LkpCountry> getLkpCountries() {
		return this.lkpCountries;
	}

	public void setLkpCountries(List<LkpCountry> lkpCountries) {
		this.lkpCountries = lkpCountries;
	}

	public LkpCountry addLkpCountry(LkpCountry lkpCountry) {
		getLkpCountries().add(lkpCountry);
		lkpCountry.setLkpStatus(this);

		return lkpCountry;
	}

	public LkpCountry removeLkpCountry(LkpCountry lkpCountry) {
		getLkpCountries().remove(lkpCountry);
		lkpCountry.setLkpStatus(null);

		return lkpCountry;
	}

	public List<LkpDebitCardType> getLkpDebitCardTypes() {
		return this.lkpDebitCardTypes;
	}

	public void setLkpDebitCardTypes(List<LkpDebitCardType> lkpDebitCardTypes) {
		this.lkpDebitCardTypes = lkpDebitCardTypes;
	}

	public LkpDebitCardType addLkpDebitCardType(LkpDebitCardType lkpDebitCardType) {
		getLkpDebitCardTypes().add(lkpDebitCardType);
		lkpDebitCardType.setLkpStatus(this);

		return lkpDebitCardType;
	}

	public LkpDebitCardType removeLkpDebitCardType(LkpDebitCardType lkpDebitCardType) {
		getLkpDebitCardTypes().remove(lkpDebitCardType);
		lkpDebitCardType.setLkpStatus(null);

		return lkpDebitCardType;
	}

	public List<LkpDisputeType> getLkpDisputeTypes() {
		return this.lkpDisputeTypes;
	}

	public void setLkpDisputeTypes(List<LkpDisputeType> lkpDisputeTypes) {
		this.lkpDisputeTypes = lkpDisputeTypes;
	}

	public LkpDisputeType addLkpDisputeType(LkpDisputeType lkpDisputeType) {
		getLkpDisputeTypes().add(lkpDisputeType);
		lkpDisputeType.setLkpStatus(this);

		return lkpDisputeType;
	}

	public LkpDisputeType removeLkpDisputeType(LkpDisputeType lkpDisputeType) {
		getLkpDisputeTypes().remove(lkpDisputeType);
		lkpDisputeType.setLkpStatus(null);

		return lkpDisputeType;
	}

	public List<LkpDistrict> getLkpDistricts() {
		return this.lkpDistricts;
	}

	public void setLkpDistricts(List<LkpDistrict> lkpDistricts) {
		this.lkpDistricts = lkpDistricts;
	}

	public LkpDistrict addLkpDistrict(LkpDistrict lkpDistrict) {
		getLkpDistricts().add(lkpDistrict);
		lkpDistrict.setLkpStatus(this);

		return lkpDistrict;
	}

	public LkpDistrict removeLkpDistrict(LkpDistrict lkpDistrict) {
		getLkpDistricts().remove(lkpDistrict);
		lkpDistrict.setLkpStatus(null);

		return lkpDistrict;
	}

	public List<LkpLanguage> getLkpLanguages() {
		return this.lkpLanguages;
	}

	public void setLkpLanguages(List<LkpLanguage> lkpLanguages) {
		this.lkpLanguages = lkpLanguages;
	}

	public LkpLanguage addLkpLanguage(LkpLanguage lkpLanguage) {
		getLkpLanguages().add(lkpLanguage);
		lkpLanguage.setLkpStatus(this);

		return lkpLanguage;
	}

	public LkpLanguage removeLkpLanguage(LkpLanguage lkpLanguage) {
		getLkpLanguages().remove(lkpLanguage);
		lkpLanguage.setLkpStatus(null);

		return lkpLanguage;
	}

	public List<LkpMessageType> getLkpMessageTypes() {
		return this.lkpMessageTypes;
	}

	public void setLkpMessageTypes(List<LkpMessageType> lkpMessageTypes) {
		this.lkpMessageTypes = lkpMessageTypes;
	}

	public LkpMessageType addLkpMessageType(LkpMessageType lkpMessageType) {
		getLkpMessageTypes().add(lkpMessageType);
		lkpMessageType.setLkpStatus(this);

		return lkpMessageType;
	}

	public LkpMessageType removeLkpMessageType(LkpMessageType lkpMessageType) {
		getLkpMessageTypes().remove(lkpMessageType);
		lkpMessageType.setLkpStatus(null);

		return lkpMessageType;
	}

	public List<LkpRegion> getLkpRegions() {
		return this.lkpRegions;
	}

	public void setLkpRegions(List<LkpRegion> lkpRegions) {
		this.lkpRegions = lkpRegions;
	}

	public LkpRegion addLkpRegion(LkpRegion lkpRegion) {
		getLkpRegions().add(lkpRegion);
		lkpRegion.setLkpStatus(this);

		return lkpRegion;
	}

	public LkpRegion removeLkpRegion(LkpRegion lkpRegion) {
		getLkpRegions().remove(lkpRegion);
		lkpRegion.setLkpStatus(null);

		return lkpRegion;
	}

	public List<LkpSegment> getLkpSegments() {
		return this.lkpSegments;
	}

	public void setLkpSegments(List<LkpSegment> lkpSegments) {
		this.lkpSegments = lkpSegments;
	}

	public LkpSegment addLkpSegment(LkpSegment lkpSegment) {
		getLkpSegments().add(lkpSegment);
		lkpSegment.setLkpStatus(this);

		return lkpSegment;
	}

	public LkpSegment removeLkpSegment(LkpSegment lkpSegment) {
		getLkpSegments().remove(lkpSegment);
		lkpSegment.setLkpStatus(null);

		return lkpSegment;
	}

	public List<LkpTaxType> getLkpTaxTypes() {
		return this.lkpTaxTypes;
	}

	public void setLkpTaxTypes(List<LkpTaxType> lkpTaxTypes) {
		this.lkpTaxTypes = lkpTaxTypes;
	}

	public LkpTaxType addLkpTaxType(LkpTaxType lkpTaxType) {
		getLkpTaxTypes().add(lkpTaxType);
		lkpTaxType.setLkpStatus(this);

		return lkpTaxType;
	}

	public LkpTaxType removeLkpTaxType(LkpTaxType lkpTaxType) {
		getLkpTaxTypes().remove(lkpTaxType);
		lkpTaxType.setLkpStatus(null);

		return lkpTaxType;
	}

	public List<TblAgent> getTblAgents() {
		return this.tblAgents;
	}

	public void setTblAgents(List<TblAgent> tblAgents) {
		this.tblAgents = tblAgents;
	}

	public TblAgent addTblAgent(TblAgent tblAgent) {
		getTblAgents().add(tblAgent);
		tblAgent.setLkpStatus(this);

		return tblAgent;
	}

	public TblAgent removeTblAgent(TblAgent tblAgent) {
		getTblAgents().remove(tblAgent);
		tblAgent.setLkpStatus(null);

		return tblAgent;
	}

	public List<TblAgentClass> getTblAgentClasses() {
		return this.tblAgentClasses;
	}

	public void setTblAgentClasses(List<TblAgentClass> tblAgentClasses) {
		this.tblAgentClasses = tblAgentClasses;
	}

	public TblAgentClass addTblAgentClass(TblAgentClass tblAgentClass) {
		getTblAgentClasses().add(tblAgentClass);
		tblAgentClass.setLkpStatus(this);

		return tblAgentClass;
	}

	public TblAgentClass removeTblAgentClass(TblAgentClass tblAgentClass) {
		getTblAgentClasses().remove(tblAgentClass);
		tblAgentClass.setLkpStatus(null);

		return tblAgentClass;
	}

	public List<TblCommissionProfile> getTblCommissionProfiles() {
		return this.tblCommissionProfiles;
	}

	public void setTblCommissionProfiles(List<TblCommissionProfile> tblCommissionProfiles) {
		this.tblCommissionProfiles = tblCommissionProfiles;
	}

	public TblCommissionProfile addTblCommissionProfile(TblCommissionProfile tblCommissionProfile) {
		getTblCommissionProfiles().add(tblCommissionProfile);
		tblCommissionProfile.setLkpStatus(this);

		return tblCommissionProfile;
	}

	public TblCommissionProfile removeTblCommissionProfile(TblCommissionProfile tblCommissionProfile) {
		getTblCommissionProfiles().remove(tblCommissionProfile);
		tblCommissionProfile.setLkpStatus(null);

		return tblCommissionProfile;
	}

	public List<TblCustomer> getTblCustomers() {
		return this.tblCustomers;
	}

	public void setTblCustomers(List<TblCustomer> tblCustomers) {
		this.tblCustomers = tblCustomers;
	}

	public TblCustomer addTblCustomer(TblCustomer tblCustomer) {
		getTblCustomers().add(tblCustomer);
		tblCustomer.setLkpStatus(this);

		return tblCustomer;
	}

	public TblCustomer removeTblCustomer(TblCustomer tblCustomer) {
		getTblCustomers().remove(tblCustomer);
		tblCustomer.setLkpStatus(null);

		return tblCustomer;
	}

	public List<TblMcConfig> getTblMcConfigs() {
		return this.tblMcConfigs;
	}

	public void setTblMcConfigs(List<TblMcConfig> tblMcConfigs) {
		this.tblMcConfigs = tblMcConfigs;
	}

	public TblMcConfig addTblMcConfig(TblMcConfig tblMcConfig) {
		getTblMcConfigs().add(tblMcConfig);
		tblMcConfig.setLkpStatus(this);

		return tblMcConfig;
	}

	public TblMcConfig removeTblMcConfig(TblMcConfig tblMcConfig) {
		getTblMcConfigs().remove(tblMcConfig);
		tblMcConfig.setLkpStatus(null);

		return tblMcConfig;
	}

	public List<TblMcPendingRequest> getTblMcPendingRequests() {
		return this.tblMcPendingRequests;
	}

	public void setTblMcPendingRequests(List<TblMcPendingRequest> tblMcPendingRequests) {
		this.tblMcPendingRequests = tblMcPendingRequests;
	}

	public TblMcPendingRequest addTblMcPendingRequest(TblMcPendingRequest tblMcPendingRequest) {
		getTblMcPendingRequests().add(tblMcPendingRequest);
		tblMcPendingRequest.setLkpStatus(this);

		return tblMcPendingRequest;
	}

	public TblMcPendingRequest removeTblMcPendingRequest(TblMcPendingRequest tblMcPendingRequest) {
		getTblMcPendingRequests().remove(tblMcPendingRequest);
		tblMcPendingRequest.setLkpStatus(null);

		return tblMcPendingRequest;
	}

	public List<TblMcRequest> getTblMcRequests() {
		return this.tblMcRequests;
	}

	public void setTblMcRequests(List<TblMcRequest> tblMcRequests) {
		this.tblMcRequests = tblMcRequests;
	}

	public TblMcRequest addTblMcRequest(TblMcRequest tblMcRequest) {
		getTblMcRequests().add(tblMcRequest);
		tblMcRequest.setLkpStatus(this);

		return tblMcRequest;
	}

	public TblMcRequest removeTblMcRequest(TblMcRequest tblMcRequest) {
		getTblMcRequests().remove(tblMcRequest);
		tblMcRequest.setLkpStatus(null);

		return tblMcRequest;
	}

	public List<TblMcRequestAction> getTblMcRequestActions() {
		return this.tblMcRequestActions;
	}

	public void setTblMcRequestActions(List<TblMcRequestAction> tblMcRequestActions) {
		this.tblMcRequestActions = tblMcRequestActions;
	}

	public TblMcRequestAction addTblMcRequestAction(TblMcRequestAction tblMcRequestAction) {
		getTblMcRequestActions().add(tblMcRequestAction);
		tblMcRequestAction.setLkpStatus(this);

		return tblMcRequestAction;
	}

	public TblMcRequestAction removeTblMcRequestAction(TblMcRequestAction tblMcRequestAction) {
		getTblMcRequestActions().remove(tblMcRequestAction);
		tblMcRequestAction.setLkpStatus(null);

		return tblMcRequestAction;
	}

	public List<TblMenu> getTblMenus() {
		return this.tblMenus;
	}

	public void setTblMenus(List<TblMenu> tblMenus) {
		this.tblMenus = tblMenus;
	}

	public TblMenu addTblMenus(TblMenu tblMenus) {
		getTblMenus().add(tblMenus);
		tblMenus.setLkpStatus(this);

		return tblMenus;
	}

	public TblMenu removeTblMenus(TblMenu tblMenus) {
		getTblMenus().remove(tblMenus);
		tblMenus.setLkpStatus(null);

		return tblMenus;
	}

	public List<TblMessage> getTblMessages() {
		return this.tblMessages;
	}

	public void setTblMessages(List<TblMessage> tblMessages) {
		this.tblMessages = tblMessages;
	}

	public TblMessage addTblMessage(TblMessage tblMessage) {
		getTblMessages().add(tblMessage);
		tblMessage.setLkpStatus(this);

		return tblMessage;
	}

	public TblMessage removeTblMessage(TblMessage tblMessage) {
		getTblMessages().remove(tblMessage);
		tblMessage.setLkpStatus(null);

		return tblMessage;
	}

	public List<TblRole> getTblRoles() {
		return this.tblRoles;
	}

	public void setTblRoles(List<TblRole> tblRoles) {
		this.tblRoles = tblRoles;
	}

	public TblRole addTblRole(TblRole tblRole) {
		getTblRoles().add(tblRole);
		tblRole.setLkpStatus(this);

		return tblRole;
	}

	public TblRole removeTblRole(TblRole tblRole) {
		getTblRoles().remove(tblRole);
		tblRole.setLkpStatus(null);

		return tblRole;
	}

	public List<TblRoleRight> getTblRoleRights() {
		return this.tblRoleRights;
	}

	public void setTblRoleRights(List<TblRoleRight> tblRoleRights) {
		this.tblRoleRights = tblRoleRights;
	}

	public TblRoleRight addTblRoleRight(TblRoleRight tblRoleRight) {
		getTblRoleRights().add(tblRoleRight);
		tblRoleRight.setLkpStatus(this);

		return tblRoleRight;
	}

	public TblRoleRight removeTblRoleRight(TblRoleRight tblRoleRight) {
		getTblRoleRights().remove(tblRoleRight);
		tblRoleRight.setLkpStatus(null);

		return tblRoleRight;
	}

	public List<TblTaxRegime> getTblTaxRegimes() {
		return this.tblTaxRegimes;
	}

	public void setTblTaxRegimes(List<TblTaxRegime> tblTaxRegimes) {
		this.tblTaxRegimes = tblTaxRegimes;
	}

	public TblTaxRegime addTblTaxRegime(TblTaxRegime tblTaxRegime) {
		getTblTaxRegimes().add(tblTaxRegime);
		tblTaxRegime.setLkpStatus(this);

		return tblTaxRegime;
	}

	public TblTaxRegime removeTblTaxRegime(TblTaxRegime tblTaxRegime) {
		getTblTaxRegimes().remove(tblTaxRegime);
		tblTaxRegime.setLkpStatus(null);

		return tblTaxRegime;
	}

	public List<TblTransCharge> getTblTransCharges() {
		return this.tblTransCharges;
	}

	public void setTblTransCharges(List<TblTransCharge> tblTransCharges) {
		this.tblTransCharges = tblTransCharges;
	}

	public TblTransCharge addTblTransCharge(TblTransCharge tblTransCharge) {
		getTblTransCharges().add(tblTransCharge);
		tblTransCharge.setLkpStatus(this);

		return tblTransCharge;
	}

	public TblTransCharge removeTblTransCharge(TblTransCharge tblTransCharge) {
		getTblTransCharges().remove(tblTransCharge);
		tblTransCharge.setLkpStatus(null);

		return tblTransCharge;
	}

	public List<TblTransDoc> getTblTransDocs() {
		return this.tblTransDocs;
	}

	public void setTblTransDocs(List<TblTransDoc> tblTransDocs) {
		this.tblTransDocs = tblTransDocs;
	}

	public TblTransDoc addTblTransDoc(TblTransDoc tblTransDoc) {
		getTblTransDocs().add(tblTransDoc);
		tblTransDoc.setLkpStatus(this);

		return tblTransDoc;
	}

	public TblTransDoc removeTblTransDoc(TblTransDoc tblTransDoc) {
		getTblTransDocs().remove(tblTransDoc);
		tblTransDoc.setLkpStatus(null);

		return tblTransDoc;
	}

	public List<TblTransLimit> getTblTransLimits() {
		return this.tblTransLimits;
	}

	public void setTblTransLimits(List<TblTransLimit> tblTransLimits) {
		this.tblTransLimits = tblTransLimits;
	}

	public TblTransLimit addTblTransLimit(TblTransLimit tblTransLimit) {
		getTblTransLimits().add(tblTransLimit);
		tblTransLimit.setLkpStatus(this);

		return tblTransLimit;
	}

	public TblTransLimit removeTblTransLimit(TblTransLimit tblTransLimit) {
		getTblTransLimits().remove(tblTransLimit);
		tblTransLimit.setLkpStatus(null);

		return tblTransLimit;
	}

	public List<TblUser> getTblUsers() {
		return this.tblUsers;
	}

	public void setTblUsers(List<TblUser> tblUsers) {
		this.tblUsers = tblUsers;
	}

	public TblUser addTblUser(TblUser tblUser) {
		getTblUsers().add(tblUser);
		tblUser.setLkpStatus(this);

		return tblUser;
	}

	public TblUser removeTblUser(TblUser tblUser) {
		getTblUsers().remove(tblUser);
		tblUser.setLkpStatus(null);

		return tblUser;
	}

}