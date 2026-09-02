package com.workflow.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_MC_REQUEST database table.
 * 
 */
@Entity
@Table(name="TBL_MC_REQUEST")
@NamedQuery(name="TblMcRequest.findAll", query="SELECT t FROM TblMcRequest t")
public class TblMcRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_MC_REQUEST_MCREQUESTID_GENERATOR", sequenceName="TBL_MC_REQUEST_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MC_REQUEST_MCREQUESTID_GENERATOR")
	@Column(name="MC_REQUEST_ID")
	private long mcRequestId;

	@Column(name="ACTION_ID")
	private String actionId;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Column(name="FORM_NAME")
	private String formName;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Temporal(TemporalType.DATE)
	@Column(name="MAKE_DATE")
	private Date makeDate;

	@Column(name="MAKER_COMMENTS")
	private String makerComments;

	@Column(name="MAKER_ID")
	private BigDecimal makerId;

	@Column(name="REF_TABLE_ID")
	private BigDecimal refTableId;

	@Column(name="REQUEST_TYPE")
	private String requestType;

	private String status;

	@Column(name="STATUS_ID")
	private BigDecimal statusId;

	@Column(name="TABLE_NAME")
	private String tableName;

	@Lob
	@Column(name="UPDATE_JSON")
	private String updateJson;

	private BigDecimal updateindex;

	@JsonIgnore
	//bi-directional many-to-one association to TblMcConfig
	@ManyToOne
	@JoinColumn(name="MC_CONFIG_ID")
	private TblMcConfig tblMcConfig;

	//bi-directional many-to-one association to TblMcRequestAction
	@OneToMany(mappedBy="tblMcRequest")
	private List<TblMcRequestAction> tblMcRequestActions;

	//bi-directional many-to-one association to TblMcRequestDetail
	@OneToMany(mappedBy="tblMcRequest")
	private List<TblMcRequestDetail> tblMcRequestDetails;

	@Column(name="OLD_JSON")
	private String oldJson;

	public long getMcRequestId() {
		return this.mcRequestId;
	}

	public void setMcRequestId(long mcRequestId) {
		this.mcRequestId = mcRequestId;
	}

	public String getActionId() {
		return this.actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
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

	public String getFormName() {
		return this.formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
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

	public Date getMakeDate() {
		return this.makeDate;
	}

	public void setMakeDate(Date makeDate) {
		this.makeDate = makeDate;
	}

	public String getMakerComments() {
		return this.makerComments;
	}

	public void setMakerComments(String makerComments) {
		this.makerComments = makerComments;
	}

	public BigDecimal getMakerId() {
		return this.makerId;
	}

	public void setMakerId(BigDecimal makerId) {
		this.makerId = makerId;
	}

	public BigDecimal getRefTableId() {
		return this.refTableId;
	}

	public void setRefTableId(BigDecimal refTableId) {
		this.refTableId = refTableId;
	}

	public String getRequestType() {
		return this.requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getStatusId() {
		return this.statusId;
	}

	public void setStatusId(BigDecimal statusId) {
		this.statusId = statusId;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public String getUpdateJson() {
		return updateJson;
	}

	public void setUpdateJson(String updateJson) {
		this.updateJson = updateJson;
	}

	public TblMcConfig getTblMcConfig() {
		return this.tblMcConfig;
	}

	public void setTblMcConfig(TblMcConfig tblMcConfig) {
		this.tblMcConfig = tblMcConfig;
	}

	public List<TblMcRequestAction> getTblMcRequestActions() {
		return this.tblMcRequestActions;
	}

	public void setTblMcRequestActions(List<TblMcRequestAction> tblMcRequestActions) {
		this.tblMcRequestActions = tblMcRequestActions;
	}

	public TblMcRequestAction addTblMcRequestAction(TblMcRequestAction tblMcRequestAction) {
		getTblMcRequestActions().add(tblMcRequestAction);
		tblMcRequestAction.setTblMcRequest(this);

		return tblMcRequestAction;
	}

	public TblMcRequestAction removeTblMcRequestAction(TblMcRequestAction tblMcRequestAction) {
		getTblMcRequestActions().remove(tblMcRequestAction);
		tblMcRequestAction.setTblMcRequest(null);

		return tblMcRequestAction;
	}

	public List<TblMcRequestDetail> getTblMcRequestDetails() {
		return this.tblMcRequestDetails;
	}

	public void setTblMcRequestDetails(List<TblMcRequestDetail> tblMcRequestDetails) {
		this.tblMcRequestDetails = tblMcRequestDetails;
	}

	public TblMcRequestDetail addTblMcRequestDetail(TblMcRequestDetail tblMcRequestDetail) {
		getTblMcRequestDetails().add(tblMcRequestDetail);
		tblMcRequestDetail.setTblMcRequest(this);

		return tblMcRequestDetail;
	}

	public TblMcRequestDetail removeTblMcRequestDetail(TblMcRequestDetail tblMcRequestDetail) {
		getTblMcRequestDetails().remove(tblMcRequestDetail);
		tblMcRequestDetail.setTblMcRequest(null);

		return tblMcRequestDetail;
	}

	public String getOldJson() {
		return oldJson;
	}

	public void setOldJson(String oldJson) {
		this.oldJson = oldJson;
	}
}