package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the TBL_MC_CONFIG_DETAIL database table.
 * 
 */
@Entity
@Table(name="TBL_MC_CONFIG_DETAIL")
@NamedQuery(name="TblMcConfigDetail.findAll", query="SELECT t FROM TblMcConfigDetail t")
public class TblMcConfigDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_MC_CONFIG_DETAIL_MCCONFIGDETAILID_GENERATOR", sequenceName="TBL_MC_CONFIG_DETAIL_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MC_CONFIG_DETAIL_MCCONFIGDETAILID_GENERATOR")
	@Column(name="MC_CONFIG_DETAIL_ID")
	private long mcConfigDetailId;

	@Column(name="APPROVAL_TYPE")
	private String approvalType;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="EMAIL_TEMPLATE_ID")
	private BigDecimal emailTemplateId;

	@Column(name="INTIMATE_ONLY")
	private String intimateOnly;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="ROLE_ID")
	private BigDecimal roleId;

	private BigDecimal seq;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblMcConfig
	@ManyToOne
	@JoinColumn(name="MC_CONFIG_ID")
	private TblMcConfig tblMcConfig;

	//bi-directional many-to-one association to TblUser
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TblUser tblUser;

	//bi-directional many-to-one association to TblMcPendingRequest
	@OneToMany(mappedBy="tblMcConfigDetail")
	private List<TblMcPendingRequest> tblMcPendingRequests;

	//bi-directional many-to-one association to TblMcRequestAction
	@OneToMany(mappedBy="tblMcConfigDetail")
	private List<TblMcRequestAction> tblMcRequestActions;

	public TblMcConfigDetail() {
	}

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

	public BigDecimal getEmailTemplateId() {
		return this.emailTemplateId;
	}

	public void setEmailTemplateId(BigDecimal emailTemplateId) {
		this.emailTemplateId = emailTemplateId;
	}

	public String getIntimateOnly() {
		return this.intimateOnly;
	}

	public void setIntimateOnly(String intimateOnly) {
		this.intimateOnly = intimateOnly;
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

	public BigDecimal getRoleId() {
		return this.roleId;
	}

	public void setRoleId(BigDecimal roleId) {
		this.roleId = roleId;
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

	public TblUser getTblUser() {
		return this.tblUser;
	}

	public void setTblUser(TblUser tblUser) {
		this.tblUser = tblUser;
	}

	public List<TblMcPendingRequest> getTblMcPendingRequests() {
		return this.tblMcPendingRequests;
	}

	public void setTblMcPendingRequests(List<TblMcPendingRequest> tblMcPendingRequests) {
		this.tblMcPendingRequests = tblMcPendingRequests;
	}

	public TblMcPendingRequest addTblMcPendingRequest(TblMcPendingRequest tblMcPendingRequest) {
		getTblMcPendingRequests().add(tblMcPendingRequest);
		tblMcPendingRequest.setTblMcConfigDetail(this);

		return tblMcPendingRequest;
	}

	public TblMcPendingRequest removeTblMcPendingRequest(TblMcPendingRequest tblMcPendingRequest) {
		getTblMcPendingRequests().remove(tblMcPendingRequest);
		tblMcPendingRequest.setTblMcConfigDetail(null);

		return tblMcPendingRequest;
	}

	public List<TblMcRequestAction> getTblMcRequestActions() {
		return this.tblMcRequestActions;
	}

	public void setTblMcRequestActions(List<TblMcRequestAction> tblMcRequestActions) {
		this.tblMcRequestActions = tblMcRequestActions;
	}

	public TblMcRequestAction addTblMcRequestAction(TblMcRequestAction tblMcRequestAction) {
		getTblMcRequestActions().add(tblMcRequestAction);
		tblMcRequestAction.setTblMcConfigDetail(this);

		return tblMcRequestAction;
	}

	public TblMcRequestAction removeTblMcRequestAction(TblMcRequestAction tblMcRequestAction) {
		getTblMcRequestActions().remove(tblMcRequestAction);
		tblMcRequestAction.setTblMcConfigDetail(null);

		return tblMcRequestAction;
	}

}