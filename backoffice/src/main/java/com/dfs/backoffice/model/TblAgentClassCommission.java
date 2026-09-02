package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_AGENT_CLASS_COMMISSION database table.
 * 
 */
@Entity
@Table(name="TBL_AGENT_CLASS_COMMISSION")
@NamedQuery(name="TblAgentClassCommission.findAll", query="SELECT t FROM TblAgentClassCommission t")
public class TblAgentClassCommission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_AGENT_CLASS_COMMISSION_AGENTCLASSCOMMISSIONID_GENERATOR", sequenceName="TBL_AGENT_CLASS_COMMISSION_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_AGENT_CLASS_COMMISSION_AGENTCLASSCOMMISSIONID_GENERATOR")
	@Column(name="AGENT_CLASS_COMMISSION_ID")
	private long agentClassCommissionId;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblAgentClass
	@ManyToOne
	@JoinColumn(name="AGENT_CLASS_ID")
	private TblAgentClass tblAgentClass;

	//bi-directional many-to-one association to TblCommissionProfile
	@ManyToOne
	@JoinColumn(name="COMMISSION_PROFILE_ID")
	private TblCommissionProfile tblCommissionProfile;

	public TblAgentClassCommission() {
	}

	public long getAgentClassCommissionId() {
		return this.agentClassCommissionId;
	}

	public void setAgentClassCommissionId(long agentClassCommissionId) {
		this.agentClassCommissionId = agentClassCommissionId;
	}

	public Timestamp getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Timestamp createdate) {
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

	public Timestamp getLastupdatedate() {
		return this.lastupdatedate;
	}

	public void setLastupdatedate(Timestamp lastupdatedate) {
		this.lastupdatedate = lastupdatedate;
	}

	public BigDecimal getLastupdateuser() {
		return this.lastupdateuser;
	}

	public void setLastupdateuser(BigDecimal lastupdateuser) {
		this.lastupdateuser = lastupdateuser;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public TblAgentClass getTblAgentClass() {
		return this.tblAgentClass;
	}

	public void setTblAgentClass(TblAgentClass tblAgentClass) {
		this.tblAgentClass = tblAgentClass;
	}

	public TblCommissionProfile getTblCommissionProfile() {
		return this.tblCommissionProfile;
	}

	public void setTblCommissionProfile(TblCommissionProfile tblCommissionProfile) {
		this.tblCommissionProfile = tblCommissionProfile;
	}

}