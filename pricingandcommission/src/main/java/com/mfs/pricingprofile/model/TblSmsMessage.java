package com.mfs.pricingprofile.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_SMS_MESSAGE database table.
 * 
 */
@Entity
@Table(name="TBL_SMS_MESSAGE")
@NamedQuery(name="TblSmsMessage.findAll", query="SELECT t FROM TblSmsMessage t")
public class TblSmsMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_SMS_MESSAGE_SMSMESSAGEID_GENERATOR", sequenceName="TBL_SMS_MESSAGE_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_SMS_MESSAGE_SMSMESSAGEID_GENERATOR")
	@Column(name="SMS_MESSAGE_ID")
	private long smsMessageId;

	private Date createdate;

	private BigDecimal createuser;
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;
	
	@Column(name = "MESSAGE")
	private String message;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	@Column(name="SEND_FLAG")
	private String sendFlag;

	@Column(name="TRANS_HEAD_ID")
	private BigDecimal transHeadId;

	private BigDecimal updateindex;

	public TblSmsMessage() {
	}

	public long getSmsMessageId() {
		return this.smsMessageId;
	}

	public void setSmsMessageId(long smsMessageId) {
		this.smsMessageId = smsMessageId;
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

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
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