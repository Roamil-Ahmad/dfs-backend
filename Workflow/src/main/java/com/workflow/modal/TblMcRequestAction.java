package com.workflow.modal;

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
	@SequenceGenerator(name="TBL_MC_REQUEST_ACTION_MCREQUESTACTIONID_GENERATOR", sequenceName="TBL_MC_REQUEST_ACTION_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MC_REQUEST_ACTION_MCREQUESTACTIONID_GENERATOR")
	@Column(name="MC_REQUEST_ACTION_ID")
	private long mcRequestActionId;

	@Temporal(TemporalType.DATE)
	@Column(name="CHECK_DATE")
	private Date checkDate;

	@Column(name="CHECKER_COMMENTS")
	private String checkerComments;

	@Column(name="CHECKER_ID")
	private BigDecimal checkerId;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MC_CONFIG_DETAIL_ID")
	private BigDecimal mcConfigDetailId;

	private BigDecimal seq;

	private String status;

	@Column(name="STATUS_ID")
	private BigDecimal statusId;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblMcPendingRequest
	@ManyToOne
	@JoinColumn(name="MC_PENDING_REQUEST_ID")
	private TblMcPendingRequest tblMcPendingRequest;

	//bi-directional many-to-one association to TblMcRequest
	@ManyToOne
	@JoinColumn(name="MC_REQUEST_ID")
	private TblMcRequest tblMcRequest;


	public long getMcRequestActionId() {
		return this.mcRequestActionId;
	}

	public void setMcRequestActionId(long mcRequestActionId) {
		this.mcRequestActionId = mcRequestActionId;
	}

	public Date getCheckDate() {
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

	public BigDecimal getCheckerId() {
		return this.checkerId;
	}

	public void setCheckerId(BigDecimal checkerId) {
		this.checkerId = checkerId;
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

	public BigDecimal getMcConfigDetailId() {
		return this.mcConfigDetailId;
	}

	public void setMcConfigDetailId(BigDecimal mcConfigDetailId) {
		this.mcConfigDetailId = mcConfigDetailId;
	}

	public BigDecimal getSeq() {
		return this.seq;
	}

	public void setSeq(BigDecimal seq) {
		this.seq = seq;
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

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
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

}