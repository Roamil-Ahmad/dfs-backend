package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_MC_REQUEST_ACTION database table.
 * 
 */
@Entity
@Table(name="TBL_MC_REQUEST_ACTION")
@NamedQuery(name="TblMcRequestAction.findAll", query="SELECT t FROM TblMcRequestAction t")
public class TblMcRequestAction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_MC_REQUEST_ACTION_MCREQUESTACTIONID_GENERATOR", sequenceName="TBL_MC_REQUEST_ACTION_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MC_REQUEST_ACTION_MCREQUESTACTIONID_GENERATOR")
	@Column(name="MC_REQUEST_ACTION_ID")
	private long mcRequestActionId;

	@Column(name="CHECK_DATE")
	private Date checkDate;

	@Column(name="CHECKER_COMMENTS")
	private String checkerComments;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal seq;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblMcConfigDetail
	@ManyToOne
	@JoinColumn(name="MC_CONFIG_DETAIL_ID")
	private TblMcConfigDetail tblMcConfigDetail;

	//bi-directional many-to-one association to TblMcPendingRequest
	@ManyToOne
	@JoinColumn(name="MC_PENDING_REQUEST_ID")
	private TblMcPendingRequest tblMcPendingRequest;

	//bi-directional many-to-one association to TblMcRequest
	@ManyToOne
	@JoinColumn(name="MC_REQUEST_ID")
	private TblMcRequest tblMcRequest;

	//bi-directional many-to-one association to TblUser
	@ManyToOne
	@JoinColumn(name="CHECKER_ID")
	private TblUser tblUser;

	public TblMcRequestAction() {
	}

	public long getMcRequestActionId() {
		return this.mcRequestActionId;
	}

	public void setMcRequestActionId(long mcRequestActionId) {
		this.mcRequestActionId = mcRequestActionId;
	}

	public Object getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckerComments() {
		return this.checkerComments;
	}

	public void setCheckerComments(String checkerComments) {
		this.checkerComments = checkerComments;
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

	public TblMcPendingRequest getTblMcPendingRequest() {
		return this.tblMcPendingRequest;
	}

	public void setTblMcPendingRequest(TblMcPendingRequest tblMcPendingRequest) {
		this.tblMcPendingRequest = tblMcPendingRequest;
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

}