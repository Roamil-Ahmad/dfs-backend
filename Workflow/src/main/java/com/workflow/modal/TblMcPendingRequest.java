package com.workflow.modal;

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
	@SequenceGenerator(name="TBL_MC_PENDING_REQUEST_MCPENDINGREQUESTID_GENERATOR", sequenceName="TBL_MC_PENDING_REQUEST_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MC_PENDING_REQUEST_MCPENDINGREQUESTID_GENERATOR")
	@Column(name="MC_PENDING_REQUEST_ID")
	private long mcPendingRequestId;

	@Column(name="ACTION_TAKEN")
	private String actionTaken;

	private String comments;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MC_REQUEST_ID")
	private BigDecimal mcRequestId;

	@Column(name="ROLE_ID")
	private BigDecimal roleId;

	private BigDecimal seq;

	private BigDecimal updateindex;

	@Column(name="USER_ID")
	private BigDecimal userId;

	//bi-directional many-to-one association to TblMcConfigDetail
	@ManyToOne
	@JoinColumn(name="MC_CONFIG_DETAIL_ID")
	private TblMcConfigDetail tblMcConfigDetail;

	//bi-directional many-to-one association to TblMcRequestAction
	@OneToMany(mappedBy="tblMcPendingRequest")
	private List<TblMcRequestAction> tblMcRequestActions;


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

	public BigDecimal getMcRequestId() {
		return this.mcRequestId;
	}

	public void setMcRequestId(BigDecimal mcRequestId) {
		this.mcRequestId = mcRequestId;
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

	public BigDecimal getUserId() {
		return this.userId;
	}

	public void setUserId(BigDecimal userId) {
		this.userId = userId;
	}

	public TblMcConfigDetail getTblMcConfigDetail() {
		return this.tblMcConfigDetail;
	}

	public void setTblMcConfigDetail(TblMcConfigDetail tblMcConfigDetail) {
		this.tblMcConfigDetail = tblMcConfigDetail;
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