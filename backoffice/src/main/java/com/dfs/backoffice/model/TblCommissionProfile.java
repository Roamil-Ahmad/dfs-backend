package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the TBL_COMMISSION_PROFILE database table.
 * 
 */
@Entity
@Table(name="TBL_COMMISSION_PROFILE")
@NamedQuery(name="TblCommissionProfile.findAll", query="SELECT t FROM TblCommissionProfile t")
public class TblCommissionProfile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_COMMISSION_PROFILE_COMMISSIONPROFILEID_GENERATOR", sequenceName="TBL_COMMISSION_PROFILE_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_COMMISSION_PROFILE_COMMISSIONPROFILEID_GENERATOR")
	@Column(name="COMMISSION_PROFILE_ID")
	private long commissionProfileId;

	@Column(name="COMMISSION_PROFILE_NAME")
	private String commissionProfileName;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_FROM")
	private Date effectiveFrom;

	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_TO")
	private Date effectiveTo;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblAgentClassCommission
	@OneToMany(mappedBy="tblCommissionProfile")
	private List<TblAgentClassCommission> tblAgentClassCommissions;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblGlAccount
	@ManyToOne
	@JoinColumn(name="GL_ACCOUNT_ID")
	private TblGlAccount tblGlAccount;

	public long getCommissionProfileId() {
		return this.commissionProfileId;
	}

	public void setCommissionProfileId(long commissionProfileId) {
		this.commissionProfileId = commissionProfileId;
	}

	public String getCommissionProfileName() {
		return this.commissionProfileName;
	}

	public void setCommissionProfileName(String commissionProfileName) {
		this.commissionProfileName = commissionProfileName;
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

	public Date getEffectiveFrom() {
		return this.effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Date getEffectiveTo() {
		return this.effectiveTo;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
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

	public List<TblAgentClassCommission> getTblAgentClassCommissions() {
		return this.tblAgentClassCommissions;
	}

	public void setTblAgentClassCommissions(List<TblAgentClassCommission> tblAgentClassCommissions) {
		this.tblAgentClassCommissions = tblAgentClassCommissions;
	}

	public TblAgentClassCommission addTblAgentClassCommission(TblAgentClassCommission tblAgentClassCommission) {
		getTblAgentClassCommissions().add(tblAgentClassCommission);
		tblAgentClassCommission.setTblCommissionProfile(this);

		return tblAgentClassCommission;
	}

	public TblAgentClassCommission removeTblAgentClassCommission(TblAgentClassCommission tblAgentClassCommission) {
		getTblAgentClassCommissions().remove(tblAgentClassCommission);
		tblAgentClassCommission.setTblCommissionProfile(null);

		return tblAgentClassCommission;
	}

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

	public TblGlAccount getTblGlAccount() {
		return this.tblGlAccount;
	}

	public void setTblGlAccount(TblGlAccount tblGlAccount) {
		this.tblGlAccount = tblGlAccount;
	}
}