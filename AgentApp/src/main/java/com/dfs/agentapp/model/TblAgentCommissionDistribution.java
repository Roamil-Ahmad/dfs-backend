package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_AGENT_COMMISSION_DISTRIBUTION database table.
 * 
 */
@Entity
@Table(name="TBL_AGENT_COMMISSION_DISTRIBUTION")
@NamedQuery(name="TblAgentCommissionDistribution.findAll", query="SELECT t FROM TblAgentCommissionDistribution t")
public class TblAgentCommissionDistribution implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_AGENT_COMMISSION_DISTRIBUTION_AGENTCOMMISSIONDISTRIBUTIONID_GENERATOR", sequenceName="TBL_AGENT_COMMISSION_DISTRIBUTION_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_AGENT_COMMISSION_DISTRIBUTION_AGENTCOMMISSIONDISTRIBUTIONID_GENERATOR")
	@Column(name="AGENT_COMMISSION_DISTRIBUTION_ID")
	private long agentCommissionDistributionId;

	@Column(name="AGENT_LEVEL")
	private BigDecimal agentLevel;

	@Column(name="COMMISSION_PERCENTAGE")
	private BigDecimal commissionPercentage;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="PARENT_AGENT_ID")
	private BigDecimal parentAgentId;

	private String status;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblAgent
	@ManyToOne
	@JoinColumn(name="AGENT_ID")
	private TblAgent tblAgent;

	public TblAgentCommissionDistribution() {
	}

	public long getAgentCommissionDistributionId() {
		return this.agentCommissionDistributionId;
	}

	public void setAgentCommissionDistributionId(long agentCommissionDistributionId) {
		this.agentCommissionDistributionId = agentCommissionDistributionId;
	}

	public BigDecimal getAgentLevel() {
		return this.agentLevel;
	}

	public void setAgentLevel(BigDecimal agentLevel) {
		this.agentLevel = agentLevel;
	}

	public BigDecimal getCommissionPercentage() {
		return this.commissionPercentage;
	}

	public void setCommissionPercentage(BigDecimal commissionPercentage) {
		this.commissionPercentage = commissionPercentage;
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

	public BigDecimal getParentAgentId() {
		return this.parentAgentId;
	}

	public void setParentAgentId(BigDecimal parentAgentId) {
		this.parentAgentId = parentAgentId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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