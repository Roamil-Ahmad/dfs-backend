package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the LKP_MESSAGE_TYPE database table.
 * 
 */
@Entity
@Table(name="LKP_MESSAGE_TYPE")
@NamedQuery(name="LkpMessageType.findAll", query="SELECT l FROM LkpMessageType l")
public class LkpMessageType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_MESSAGE_TYPE_MESSAGETYPEID_GENERATOR", sequenceName="LKP_MESSAGE_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_MESSAGE_TYPE_MESSAGETYPEID_GENERATOR")
	@Column(name="MESSAGE_TYPE_ID")
	private long messageTypeId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MESSAGE_TYPE_CODE")
	private String messageTypeCode;

	@Column(name="MESSAGE_TYPE_DESCR")
	private String messageTypeDescr;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblMessage
	@OneToMany(mappedBy="lkpMessageType")
	private List<TblMessage> tblMessages;

	public LkpMessageType() {
	}

	public long getMessageTypeId() {
		return this.messageTypeId;
	}

	public void setMessageTypeId(long messageTypeId) {
		this.messageTypeId = messageTypeId;
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

	public String getMessageTypeCode() {
		return this.messageTypeCode;
	}

	public void setMessageTypeCode(String messageTypeCode) {
		this.messageTypeCode = messageTypeCode;
	}

	public String getMessageTypeDescr() {
		return this.messageTypeDescr;
	}

	public void setMessageTypeDescr(String messageTypeDescr) {
		this.messageTypeDescr = messageTypeDescr;
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

	public List<TblMessage> getTblMessages() {
		return this.tblMessages;
	}

	public void setTblMessages(List<TblMessage> tblMessages) {
		this.tblMessages = tblMessages;
	}

	public TblMessage addTblMessage(TblMessage tblMessage) {
		getTblMessages().add(tblMessage);
		tblMessage.setLkpMessageType(this);

		return tblMessage;
	}

	public TblMessage removeTblMessage(TblMessage tblMessage) {
		getTblMessages().remove(tblMessage);
		tblMessage.setLkpMessageType(null);

		return tblMessage;
	}

}