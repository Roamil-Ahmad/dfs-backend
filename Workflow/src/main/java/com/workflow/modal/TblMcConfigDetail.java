package com.workflow.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_MC_CONFIG_DETAIL database table.
 * 
 */
@Entity(name="TblMcConfigDetail")
@Table(name="TBL_MC_CONFIG_DETAIL")
@NamedQuery(name="TblMcConfigDetail.findAll", query="SELECT t FROM TblMcConfigDetail t")
public class TblMcConfigDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_MC_CONFIG_DETAIL_MCCONFIGDETAILID_GENERATOR", sequenceName="TBL_MC_CONFIG_DETAIL_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MC_CONFIG_DETAIL_MCCONFIGDETAILID_GENERATOR")
	@Column(name="MC_CONFIG_DETAIL_ID")
	private long mcConfigDetailId;

	@Column(name="APPROVAL_TYPE")
	private String approvalType;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Column(name="INTIMATE_ONLY")
	private String intimateOnly;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	//bi-directional many-to-one association to TblRole
	@ManyToOne
	@JoinColumn(name="ROLE_ID")
	private TblRole tblRole;

	private BigDecimal seq;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblRole
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TblUser tblUser;

	@Transient
	private String approvalCriteria;

	@JsonIgnore
	//bi-directional many-to-one association to TblMcConfig
	@ManyToOne
	@JoinColumn(name="MC_CONFIG_ID")
	private TblMcConfig tblMcConfig;

	@Column(name="IS_ACTIVE")
	private String isActive;

	public long getMcConfigDetailId() {
		return this.mcConfigDetailId;
	}

	public void setMcConfigDetailId(long mcConfigDetailId) {
		this.mcConfigDetailId = mcConfigDetailId;
	}

	public String getApprovalType() {
		return this.approvalType;
	}

	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
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

	public String getIntimateOnly() {
		return this.intimateOnly;
	}

	public void setIntimateOnly(String intimateOnly) {
		this.intimateOnly = intimateOnly;
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

	public BigDecimal getSeq() {
		return this.seq;
	}

	public void setSeq(BigDecimal seq) {
		this.seq = seq;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public TblMcConfig getTblMcConfig() {
		return this.tblMcConfig;
	}

	public void setTblMcConfig(TblMcConfig tblMcConfig) {
		this.tblMcConfig = tblMcConfig;
	}

	public String getApprovalCriteria() {
		return approvalCriteria;
	}

	public void setApprovalCriteria(String approvalCriteria) {
		this.approvalCriteria = approvalCriteria;
	}

	public TblRole getTblRole() {
		return tblRole;
	}

	public void setTblRole(TblRole tblRole) {
		this.tblRole = tblRole;
	}

	public TblUser getTblUser() {
		return tblUser;
	}

	public void setTblUser(TblUser tblUser) {
		this.tblUser = tblUser;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
}