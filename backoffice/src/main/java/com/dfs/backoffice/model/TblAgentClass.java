package com.dfs.backoffice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
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

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblAgentClassCharge
	@JsonIgnore
	@OneToMany(mappedBy="tblAgentClass")
	private List<TblAgentClassCharge> tblAgentClassCharges;

	//bi-directional many-to-one association to TblAgentClassCommission
	@JsonIgnore
	@OneToMany(mappedBy="tblAgentClass")
	private List<TblAgentClassCommission> tblAgentClassCommissions;

	public TblAgentClass() {
	}

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

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

	public List<TblAgentClassCharge> getTblAgentClassCharges() {
		return this.tblAgentClassCharges;
	}

	public void setTblAgentClassCharges(List<TblAgentClassCharge> tblAgentClassCharges) {
		this.tblAgentClassCharges = tblAgentClassCharges;
	}

	public TblAgentClassCharge addTblAgentClassCharge(TblAgentClassCharge tblAgentClassCharge) {
		getTblAgentClassCharges().add(tblAgentClassCharge);
		tblAgentClassCharge.setTblAgentClass(this);

		return tblAgentClassCharge;
	}

	public TblAgentClassCharge removeTblAgentClassCharge(TblAgentClassCharge tblAgentClassCharge) {
		getTblAgentClassCharges().remove(tblAgentClassCharge);
		tblAgentClassCharge.setTblAgentClass(null);

		return tblAgentClassCharge;
	}

	public List<TblAgentClassCommission> getTblAgentClassCommissions() {
		return this.tblAgentClassCommissions;
	}

	public void setTblAgentClassCommissions(List<TblAgentClassCommission> tblAgentClassCommissions) {
		this.tblAgentClassCommissions = tblAgentClassCommissions;
	}

	public TblAgentClassCommission addTblAgentClassCommission(TblAgentClassCommission tblAgentClassCommission) {
		getTblAgentClassCommissions().add(tblAgentClassCommission);
		tblAgentClassCommission.setTblAgentClass(this);

		return tblAgentClassCommission;
	}

	public TblAgentClassCommission removeTblAgentClassCommission(TblAgentClassCommission tblAgentClassCommission) {
		getTblAgentClassCommissions().remove(tblAgentClassCommission);
		tblAgentClassCommission.setTblAgentClass(null);

		return tblAgentClassCommission;
	}

}