package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_SMS_MESSAGE_TEMPLATE database table.
 * 
 */
@Entity
@Table(name="TBL_SMS_MESSAGE_TEMPLATE")
@NamedQuery(name="TblSmsMessageTemplate.findAll", query="SELECT t FROM TblSmsMessageTemplate t")
public class TblSmsMessageTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_SMS_MESSAGE_TEMPLATE_SMSMESSAGETEMPLATEID_GENERATOR", sequenceName="TBL_SMS_MESSAGE_TEMPLATE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_SMS_MESSAGE_TEMPLATE_SMSMESSAGETEMPLATEID_GENERATOR")
	@Column(name="SMS_MESSAGE_TEMPLATE_ID")
	private long smsMessageTemplateId;

	private Date createdate;

	private BigDecimal createuser;

	private String identifier;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MESSAGE_TEMPLATE")
	private String messageTemplate;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblTransDoc
	@ManyToOne
	@JoinColumn(name="TRANS_DOCS_ID")
	private TblTransDoc tblTransDoc;

	public TblSmsMessageTemplate() {
	}

	public long getSmsMessageTemplateId() {
		return this.smsMessageTemplateId;
	}

	public void setSmsMessageTemplateId(long smsMessageTemplateId) {
		this.smsMessageTemplateId = smsMessageTemplateId;
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

	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
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

	public String getMessageTemplate() {
		return this.messageTemplate;
	}

	public void setMessageTemplate(String messageTemplate) {
		this.messageTemplate = messageTemplate;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public TblTransDoc getTblTransDoc() {
		return this.tblTransDoc;
	}

	public void setTblTransDoc(TblTransDoc tblTransDoc) {
		this.tblTransDoc = tblTransDoc;
	}

}