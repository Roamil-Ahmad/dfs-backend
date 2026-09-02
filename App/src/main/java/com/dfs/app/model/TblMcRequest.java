package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;
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
	@SequenceGenerator(name="TBL_MC_REQUEST_MCREQUESTID_GENERATOR", sequenceName="TBL_MC_REQUEST_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MC_REQUEST_MCREQUESTID_GENERATOR")
	@Column(name="MC_REQUEST_ID")
	private long mcRequestId;

	@Column(name="ACTION_ID")
	private String actionId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="FORM_NAME")
	private String formName;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MAKE_DATE")
	private Date makeDate;

	@Column(name="MAKER_COMMENTS")
	private String makerComments;

	@Lob
	@Column(name="OLD_JSON")
	private String oldJson;

	@Column(name="REF_CREATEDATE")
	private Date refCreatedate;

	@Column(name="REF_CREATEUSER")
	private BigDecimal refCreateuser;

	@Column(name="REF_TABLE_ID")
	private BigDecimal refTableId;

	@Column(name="REF_UPDATEUSER")
	private BigDecimal refUpdateuser;

	@Column(name="REQUEST_TYPE")
	private String requestType;

	private String reserve1;

	private String status;

	@Column(name="TABLE_NAME")
	private String tableName;

	@Lob
	@Column(name="UPDATE_JSON")
	private String updateJson;

	@Column(name="UPDATE_TYPE")
	private String updateType;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblMcPendingRequest
	@OneToMany(mappedBy="tblMcRequest")
	private List<TblMcPendingRequest> tblMcPendingRequests;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblMcConfig
	@ManyToOne
	@JoinColumn(name="MC_CONFIG_ID")
	private TblMcConfig tblMcConfig;

	//bi-directional many-to-one association to TblUser
	@ManyToOne
	@JoinColumn(name="MAKER_ID")
	private TblUser tblUser;

	//bi-directional many-to-one association to TblMcRequestAction
	@OneToMany(mappedBy="tblMcRequest")
	private List<TblMcRequestAction> tblMcRequestActions;

	public TblMcRequest() {
	}

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

	public Object getMakeDate() {
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

	public String getOldJson() {
		return this.oldJson;
	}

	public void setOldJson(String oldJson) {
		this.oldJson = oldJson;
	}

	public Date getRefCreatedate() {
		return this.refCreatedate;
	}

	public void setRefCreatedate(Date refCreatedate) {
		this.refCreatedate = refCreatedate;
	}

	public BigDecimal getRefCreateuser() {
		return this.refCreateuser;
	}

	public void setRefCreateuser(BigDecimal refCreateuser) {
		this.refCreateuser = refCreateuser;
	}

	public BigDecimal getRefTableId() {
		return this.refTableId;
	}

	public void setRefTableId(BigDecimal refTableId) {
		this.refTableId = refTableId;
	}

	public BigDecimal getRefUpdateuser() {
		return this.refUpdateuser;
	}

	public void setRefUpdateuser(BigDecimal refUpdateuser) {
		this.refUpdateuser = refUpdateuser;
	}

	public String getRequestType() {
		return this.requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getReserve1() {
		return this.reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getUpdateJson() {
		return this.updateJson;
	}

	public void setUpdateJson(String updateJson) {
		this.updateJson = updateJson;
	}

	public String getUpdateType() {
		return this.updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public List<TblMcPendingRequest> getTblMcPendingRequests() {
		return this.tblMcPendingRequests;
	}

	public void setTblMcPendingRequests(List<TblMcPendingRequest> tblMcPendingRequests) {
		this.tblMcPendingRequests = tblMcPendingRequests;
	}

	public TblMcPendingRequest addTblMcPendingRequest(TblMcPendingRequest tblMcPendingRequest) {
		getTblMcPendingRequests().add(tblMcPendingRequest);
		tblMcPendingRequest.setTblMcRequest(this);

		return tblMcPendingRequest;
	}

	public TblMcPendingRequest removeTblMcPendingRequest(TblMcPendingRequest tblMcPendingRequest) {
		getTblMcPendingRequests().remove(tblMcPendingRequest);
		tblMcPendingRequest.setTblMcRequest(null);

		return tblMcPendingRequest;
	}

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
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

}