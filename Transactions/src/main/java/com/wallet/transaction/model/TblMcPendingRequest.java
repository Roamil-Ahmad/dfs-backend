package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_MC_PENDING_REQUEST database table.
 * 
 */
@Entity
@Table(name="TBL_MC_PENDING_REQUEST")
@NamedQuery(name="TblMcPendingRequest.findAll", query="SELECT t FROM TblMcPendingRequest t")
public class TblMcPendingRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_MC_PENDING_REQUEST_MCPENDINGREQUESTID_GENERATOR", sequenceName="TBL_MC_PENDING_REQUEST_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MC_PENDING_REQUEST_MCPENDINGREQUESTID_GENERATOR")
	@Column(name="MC_PENDING_REQUEST_ID")
	private long mcPendingRequestId;

	@Column(name="ACTION_TAKEN")
	private String actionTaken;

	private String comments;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="ROLE_ID")
	private BigDecimal roleId;

	private BigDecimal seq;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblMcNotification
	@OneToMany(mappedBy="tblMcPendingRequest")
	private List<TblMcNotification> tblMcNotifications;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblMcConfigDetail
	@ManyToOne
	@JoinColumn(name="MC_CONFIG_DETAIL_ID")
	private TblMcConfigDetail tblMcConfigDetail;

	//bi-directional many-to-one association to TblMcRequest
	@ManyToOne
	@JoinColumn(name="MC_REQUEST_ID")
	private TblMcRequest tblMcRequest;

	//bi-directional many-to-one association to TblUser
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TblUser tblUser;

	//bi-directional many-to-one association to TblMcRequestAction
	@OneToMany(mappedBy="tblMcPendingRequest")
	private List<TblMcRequestAction> tblMcRequestActions;

	public TblMcPendingRequest() {
	}

	public long getMcPendingRequestId() {
		return this.mcPendingRequestId;
	}

	public void setMcPendingRequestId(long mcPendingRequestId) {
		this.mcPendingRequestId = mcPendingRequestId;
	}

	public String getActionTaken() {
		return this.actionTaken;
	}

	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public List<TblMcNotification> getTblMcNotifications() {
		return this.tblMcNotifications;
	}

	public void setTblMcNotifications(List<TblMcNotification> tblMcNotifications) {
		this.tblMcNotifications = tblMcNotifications;
	}

	public TblMcNotification addTblMcNotification(TblMcNotification tblMcNotification) {
		getTblMcNotifications().add(tblMcNotification);
		tblMcNotification.setTblMcPendingRequest(this);

		return tblMcNotification;
	}

	public TblMcNotification removeTblMcNotification(TblMcNotification tblMcNotification) {
		getTblMcNotifications().remove(tblMcNotification);
		tblMcNotification.setTblMcPendingRequest(null);

		return tblMcNotification;
	}

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

	public TblMcConfigDetail getTblMcConfigDetail() {
		return this.tblMcConfigDetail;
	}

	public void setTblMcConfigDetail(TblMcConfigDetail tblMcConfigDetail) {
		this.tblMcConfigDetail = tblMcConfigDetail;
	}

	public TblMcRequest getTblMcRequest() {
		return this.tblMcRequest;
	}

	public void setTblMcRequest(TblMcRequest tblMcRequest) {
		this.tblMcRequest = tblMcRequest;
	}

	public TblUser getTblUser() {
		return this.tblUser;
	}

	public void setTblUser(TblUser tblUser) {
		this.tblUser = tblUser;
	}

	public List<TblMcRequestAction> getTblMcRequestActions() {
		return this.tblMcRequestActions;
	}

	public void setTblMcRequestActions(List<TblMcRequestAction> tblMcRequestActions) {
		this.tblMcRequestActions = tblMcRequestActions;
	}

	public TblMcRequestAction addTblMcRequestAction(TblMcRequestAction tblMcRequestAction) {
		getTblMcRequestActions().add(tblMcRequestAction);
		tblMcRequestAction.setTblMcPendingRequest(this);

		return tblMcRequestAction;
	}

	public TblMcRequestAction removeTblMcRequestAction(TblMcRequestAction tblMcRequestAction) {
		getTblMcRequestActions().remove(tblMcRequestAction);
		tblMcRequestAction.setTblMcPendingRequest(null);

		return tblMcRequestAction;
	}

}