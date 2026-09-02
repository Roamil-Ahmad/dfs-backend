package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_EMAIL_TEMPLATE database table.
 * 
 */
@Entity
@Table(name="TBL_EMAIL_TEMPLATE")
@NamedQuery(name="TblEmailTemplate.findAll", query="SELECT t FROM TblEmailTemplate t")
public class TblEmailTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_EMAIL_TEMPLATE_EMAILTEMPLATEID_GENERATOR", sequenceName="TBL_EMAIL_TEMPLATE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_EMAIL_TEMPLATE_EMAILTEMPLATEID_GENERATOR")
	@Column(name="EMAIL_TEMPLATE_ID")
	private long emailTemplateId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Lob
	private String template;

	@Column(name="TEMPLATE_NAME")
	private String templateName;

	private BigDecimal updateindex;

	public TblEmailTemplate() {
	}

	public long getEmailTemplateId() {
		return this.emailTemplateId;
	}

	public void setEmailTemplateId(long emailTemplateId) {
		this.emailTemplateId = emailTemplateId;
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

	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}