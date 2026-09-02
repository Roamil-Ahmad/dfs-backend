package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_OTPS database table.
 * 
 */
@Entity
@Table(name="TBL_OTPS")
@NamedQuery(name="TblOtp.findAll", query="SELECT t FROM TblOtp t")
public class TblOtp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_OTPS_OTPSID_GENERATOR", sequenceName="TBL_OTPS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_OTPS_OTPSID_GENERATOR")
	@Column(name="OTPS_ID")
	private long otpsId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="CUSTOMER_ID")
	private BigDecimal customerId;

	@Column(name="EFFECTIVE_FROM")
	private Date effectiveFrom;

	@Column(name="EFFECTIVE_TO")
	private Date effectiveTo;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="OTP_TRIES")
	private BigDecimal otpTries;

	@Column(name="OTP_TYPE")
	private String otpType;

	private String otpin;

	private BigDecimal updateindex;

	private String verified;

	//bi-directional many-to-one association to TblAppUser
	@ManyToOne
	@JoinColumn(name="APP_USER_ID")
	private TblAppUser tblAppUser;

	//bi-directional many-to-one association to TblCustomerAll
	@ManyToOne
	@JoinColumn(name="CUSTOMER_ALL_ID")
	private TblCustomerAll tblCustomerAll;

	@Column(name="AGENT_ID")
	private BigDecimal agentId;

	public TblOtp() {
	}

	public long getOtpsId() {
		return this.otpsId;
	}

	public void setOtpsId(long otpsId) {
		this.otpsId = otpsId;
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

	public BigDecimal getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public Date getEffectiveFrom() {
		return this.effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Date getEffectiveTo() {
		return this.effectiveTo;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
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

	public BigDecimal getOtpTries() {
		return this.otpTries;
	}

	public void setOtpTries(BigDecimal otpTries) {
		this.otpTries = otpTries;
	}

	public String getOtpType() {
		return this.otpType;
	}

	public void setOtpType(String otpType) {
		this.otpType = otpType;
	}

	public String getOtpin() {
		return this.otpin;
	}

	public void setOtpin(String otpin) {
		this.otpin = otpin;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public String getVerified() {
		return this.verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public TblAppUser getTblAppUser() {
		return this.tblAppUser;
	}

	public void setTblAppUser(TblAppUser tblAppUser) {
		this.tblAppUser = tblAppUser;
	}

	public TblCustomerAll getTblCustomerAll() {
		return this.tblCustomerAll;
	}

	public void setTblCustomerAll(TblCustomerAll tblCustomerAll) {
		this.tblCustomerAll = tblCustomerAll;
	}

	public BigDecimal getAgentId() {
		return agentId;
	}

	public void setAgentId(BigDecimal agentId) {
		this.agentId = agentId;
	}
}