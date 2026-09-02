package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_COMPLAINT database table.
 * 
 */
@Entity
@Table(name="TBL_COMPLAINT")
@NamedQuery(name="TblComplaint.findAll", query="SELECT t FROM TblComplaint t")
public class TblComplaint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_COMPLAINT_COMPLAINTID_GENERATOR", sequenceName="TBL_COMPLAINT_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_COMPLAINT_COMPLAINTID_GENERATOR")
	@Column(name="COMPLAINT_ID")
	private long complaintId;

	@Column(name="APP_USER_ID")
	private BigDecimal appUserId;

	@Column(name="COMPLAINT_DOCUMENT_ID")
	private BigDecimal complaintDocumentId;

	@Column(name="CONTACT_OPTION")
	private String contactOption;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="ISSUE_DESCR")
	private String issueDescr;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private String status;

	@Column(name="TICKET_ID")
	private String ticketId;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpIssueType
	@ManyToOne
	@JoinColumn(name="ISSUE_TYPE_ID")
	private LkpIssueType lkpIssueType;

	public TblComplaint() {
	}

	public long getComplaintId() {
		return this.complaintId;
	}

	public void setComplaintId(long complaintId) {
		this.complaintId = complaintId;
	}

	public BigDecimal getAppUserId() {
		return this.appUserId;
	}

	public void setAppUserId(BigDecimal appUserId) {
		this.appUserId = appUserId;
	}

	public BigDecimal getComplaintDocumentId() {
		return this.complaintDocumentId;
	}

	public void setComplaintDocumentId(BigDecimal complaintDocumentId) {
		this.complaintDocumentId = complaintDocumentId;
	}

	public String getContactOption() {
		return this.contactOption;
	}

	public void setContactOption(String contactOption) {
		this.contactOption = contactOption;
	}


	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public void setLastupdatedate(Date lastupdatedate) {
		this.lastupdatedate = lastupdatedate;
	}

	public BigDecimal getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(BigDecimal createuser) {
		this.createuser = createuser;
	}

	public String getIssueDescr() {
		return this.issueDescr;
	}

	public void setIssueDescr(String issueDescr) {
		this.issueDescr = issueDescr;
	}

	public BigDecimal getLastupdateuser() {
		return this.lastupdateuser;
	}

	public void setLastupdateuser(BigDecimal lastupdateuser) {
		this.lastupdateuser = lastupdateuser;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTicketId() {
		return this.ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public LkpIssueType getLkpIssueType() {
		return this.lkpIssueType;
	}

	public void setLkpIssueType(LkpIssueType lkpIssueType) {
		this.lkpIssueType = lkpIssueType;
	}

}