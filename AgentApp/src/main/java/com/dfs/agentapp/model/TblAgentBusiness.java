package com.dfs.agentapp.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the TBL_AGENT_BUSINESS database table.
 * 
 */
@Entity
@Table(name="TBL_AGENT_BUSINESS")
@NamedQuery(name="TblAgentBusiness.findAll", query="SELECT t FROM TblAgentBusiness t")
public class TblAgentBusiness implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_AGENT_BUSINESS_AGENTBUSINESSID_GENERATOR", sequenceName="TBL_AGENT_BUSINESS_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_AGENT_BUSINESS_AGENTBUSINESSID_GENERATOR")
	@Column(name="AGENT_BUSINESS_ID")
	private long agentBusinessId;

	@Column(name="BUSINESS_NAME")
	private String businessName;

	@Column(name="BUSINESS_TYPE_ID")
	private BigDecimal businessTypeId;

	@Column(name="BUSINESS_ADDRESS")
	private String businessAddress;

	@Column(name="CITY_ID")
	private BigDecimal cityId;

	@Column(name="EXPECTED_MONTHLY_VOLUME_ID")
	private BigDecimal expectedMonthlyVolumeId;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private String latitude;

	private String longitude;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblAgent
	@ManyToOne
	@JoinColumn(name="AGENT_ID")
	private TblAgent tblAgent;

	public TblAgentBusiness() {
	}

	public long getAgentBusinessId() {
		return this.agentBusinessId;
	}

	public void setAgentBusinessId(long agentBusinessId) {
		this.agentBusinessId = agentBusinessId;
	}

	public String getBusinessName() {
		return this.businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public BigDecimal getBusinessTypeId() {
		return this.businessTypeId;
	}

	public void setBusinessTypeId(BigDecimal businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getBusinessAddress() {
		return this.businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public BigDecimal getCityId() {
		return this.cityId;
	}

	public void setCityId(BigDecimal cityId) {
		this.cityId = cityId;
	}

	public BigDecimal getExpectedMonthlyVolumeId() {
		return this.expectedMonthlyVolumeId;
	}

	public void setExpectedMonthlyVolumeId(BigDecimal expectedMonthlyVolumeId) {
		this.expectedMonthlyVolumeId = expectedMonthlyVolumeId;
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

	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
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

}