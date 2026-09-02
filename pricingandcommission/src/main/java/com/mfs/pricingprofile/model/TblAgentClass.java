package com.mfs.pricingprofile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_AGENT_CLASS database table.
 * 
 */
@Entity
@Table(name="TBL_AGENT_CLASS")
@NamedQuery(name="TblAgentClass.findAll", query="SELECT t FROM TblAgentClass t")
public class TblAgentClass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_AGENT_CLASS_AGENTCLASSID_GENERATOR", sequenceName="TBL_AGENT_CLASS_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_AGENT_CLASS_AGENTCLASSID_GENERATOR")
	@Column(name="AGENT_CLASS_ID")
	private long agentClassId;

	@Column(name="AGENT_CLASS_CODE")
	private String agentClassCode;

	@Column(name="AGENT_CLASS_DESCR")
	private String agentClassDescr;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="STATUS_ID")
	private BigDecimal statusId;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private BigDecimal updateindex;

	@JsonIgnore
	//bi-directional many-to-one association to TblAgent
	@OneToMany(mappedBy="tblAgentClass")
	private List<TblAgent> tblAgents;

	public long getAgentClassId() {
		return this.agentClassId;
	}

	public void setAgentClassId(long agentClassId) {
		this.agentClassId = agentClassId;
	}

	public String getAgentClassCode() {
		return this.agentClassCode;
	}

	public void setAgentClassCode(String agentClassCode) {
		this.agentClassCode = agentClassCode;
	}

	public String getAgentClassDescr() {
		return this.agentClassDescr;
	}

	public void setAgentClassDescr(String agentClassDescr) {
		this.agentClassDescr = agentClassDescr;
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

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public List<TblAgent> getTblAgents() {
		return this.tblAgents;
	}

	public void setTblAgents(List<TblAgent> tblAgents) {
		this.tblAgents = tblAgents;
	}

	public TblAgent addTblAgent(TblAgent tblAgent) {
		getTblAgents().add(tblAgent);
		tblAgent.setTblAgentClass(this);

		return tblAgent;
	}

	public TblAgent removeTblAgent(TblAgent tblAgent) {
		getTblAgents().remove(tblAgent);
		tblAgent.setTblAgentClass(null);

		return tblAgent;
	}

}