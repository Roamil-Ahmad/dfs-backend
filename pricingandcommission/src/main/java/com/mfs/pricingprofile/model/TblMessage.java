package com.mfs.pricingprofile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the TBL_MESSAGE database table.
 * 
 */
@Entity
@Table(name = "TBL_MESSAGE")
@NamedQuery(name = "TblMessage.findAll", query = "SELECT t FROM TblMessage t")
public class TblMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "TBL_MESSAGE_MESSAGEID_GENERATOR", sequenceName = "TBL_MESSAGE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_MESSAGE_MESSAGEID_GENERATOR")
	@Column(name = "MESSAGE_ID")
	private long messageId;

	@Column(name = "APPROVED_YN")
	private String approvedYn;

	// //
	@JsonIgnore
	private Date createdate;

	@JsonIgnore
	private BigDecimal createuser;

	// //
	@JsonIgnore
	private Date lastupdatedate;

	@JsonIgnore
	private BigDecimal lastupdateuser;

	@Column(name = "MESSAGE_CODE")
	private String messageCode;

	@Column(name = "MESSAGE_DESCR")
	private String messageDescr;

	private String isActive;

	@JsonIgnore
	private BigDecimal updateindex;

	// bi-directional many-to-one association to LkpMessageType
	@ManyToOne
	@JoinColumn(name = "LKP_MESSAGE_TYPE_ID")
	private LkpMessageType lkpMessageType;

	public TblMessage() {
		this.lkpMessageType = new LkpMessageType();
	}

	public long getMessageId() {
		return this.messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public String getApprovedYn() {
		return this.approvedYn;
	}

	public void setApprovedYn(String approvedYn) {
		this.approvedYn = approvedYn;
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

	public String getMessageCode() {
		return this.messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getMessageDescr() {
		return this.messageDescr;
	}

	public void setMessageDescr(String messageDescr) {
		this.messageDescr = messageDescr;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public LkpMessageType getLkpMessageType() {
		return this.lkpMessageType;
	}

	public void setLkpMessageType(LkpMessageType lkpMessageType) {
		this.lkpMessageType = lkpMessageType;
	}

}