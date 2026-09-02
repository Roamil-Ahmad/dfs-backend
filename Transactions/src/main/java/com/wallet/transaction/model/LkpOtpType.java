package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the LKP_OTP_TYPE database table.
 * 
 */
@Entity
@Table(name="LKP_OTP_TYPE")
@NamedQuery(name="LkpOtpType.findAll", query="SELECT l FROM LkpOtpType l")
public class LkpOtpType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_OTP_TYPE_OTPTYPEID_GENERATOR", sequenceName="LKP_OTP_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_OTP_TYPE_OTPTYPEID_GENERATOR")
	@Column(name="OTP_TYPE_ID")
	private long otpTypeId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DAYS_BLOCKED")
	private BigDecimal daysBlocked;

	@Column(name="EXPIRY_MINUTES")
	private BigDecimal expiryMinutes;

	@Column(name="EXPIRY_TRIES")
	private BigDecimal expiryTries;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="OTP_ATTEMPTS")
	private BigDecimal otpAttempts;

	@Column(name="OTP_DESCR")
	private String otpDescr;

	@Column(name="OTP_TYPE")
	private String otpType;

	@Column(name="REGULAR_EXPRESSION")
	private String regularExpression;

	private BigDecimal updateindex;

	public LkpOtpType() {
	}

	public long getOtpTypeId() {
		return this.otpTypeId;
	}

	public void setOtpTypeId(long otpTypeId) {
		this.otpTypeId = otpTypeId;
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

	public BigDecimal getDaysBlocked() {
		return this.daysBlocked;
	}

	public void setDaysBlocked(BigDecimal daysBlocked) {
		this.daysBlocked = daysBlocked;
	}

	public BigDecimal getExpiryMinutes() {
		return this.expiryMinutes;
	}

	public void setExpiryMinutes(BigDecimal expiryMinutes) {
		this.expiryMinutes = expiryMinutes;
	}

	public BigDecimal getExpiryTries() {
		return this.expiryTries;
	}

	public void setExpiryTries(BigDecimal expiryTries) {
		this.expiryTries = expiryTries;
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

	public BigDecimal getOtpAttempts() {
		return this.otpAttempts;
	}

	public void setOtpAttempts(BigDecimal otpAttempts) {
		this.otpAttempts = otpAttempts;
	}

	public String getOtpDescr() {
		return this.otpDescr;
	}

	public void setOtpDescr(String otpDescr) {
		this.otpDescr = otpDescr;
	}

	public String getOtpType() {
		return this.otpType;
	}

	public void setOtpType(String otpType) {
		this.otpType = otpType;
	}

	public String getRegularExpression() {
		return this.regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}