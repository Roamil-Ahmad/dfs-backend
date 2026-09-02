package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_EMAIL database table.
 * 
 */
@Entity
@Table(name="TBL_EMAIL")
@NamedQuery(name="TblEmail.findAll", query="SELECT t FROM TblEmail t")
public class TblEmail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_EMAIL_EMAILID_GENERATOR", sequenceName="TBL_EMAIL_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_EMAIL_EMAILID_GENERATOR")
	@Column(name="EMAIL_ID")
	private long emailId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="EMAIL_ADDRESS")
	private String emailAddress;

	@Column(name="EMAIL_SUBJECT")
	private String emailSubject;

	@Lob
	@Column(name="EMAIL_TEXT")
	private String emailText;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="SEND_FLAG")
	private String sendFlag;

	@Column(name="TRANS_HEAD_ID")
	private BigDecimal transHeadId;

	private BigDecimal updateindex;

	public TblEmail() {
	}

	public long getEmailId() {
		return this.emailId;
	}

	public void setEmailId(long emailId) {
		this.emailId = emailId;
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

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getEmailSubject() {
		return this.emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailText() {
		return this.emailText;
	}

	public void setEmailText(String emailText) {
		this.emailText = emailText;
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

	public String getSendFlag() {
		return this.sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}

	public BigDecimal getTransHeadId() {
		return this.transHeadId;
	}

	public void setTransHeadId(BigDecimal transHeadId) {
		this.transHeadId = transHeadId;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}