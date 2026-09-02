package com.wallet.transaction.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the LKP_BANK database table.
 * 
 */
@Entity
@Table(name="LKP_BANK")
@NamedQuery(name="LkpBank.findAll", query="SELECT l FROM LkpBank l")
public class LkpBank implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_BANK_BANKID_GENERATOR", sequenceName="LKP_BANK_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_BANK_BANKID_GENERATOR")
	@Column(name="BANK_ID")
	private long bankId;

	@Column(name="BANK_CODE")
	private String bankCode;

	@Column(name="BANK_IMD")
	private String bankImd;

	@Column(name="BANK_NAME")
	private String bankName;

	@Column(name="BIC_CODE")
	private String bicCode;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IBAN_SHORT")
	private String ibanShort;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MAX_ACC_LENGTH")
	private BigDecimal maxAccLength;

	@Column(name="MIN_ACC_LENGTH")
	private BigDecimal minAccLength;

	private BigDecimal updateindex;

	public LkpBank() {
	}

	public long getBankId() {
		return this.bankId;
	}

	public void setBankId(long bankId) {
		this.bankId = bankId;
	}

	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankImd() {
		return this.bankImd;
	}

	public void setBankImd(String bankImd) {
		this.bankImd = bankImd;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBicCode() {
		return this.bicCode;
	}

	public void setBicCode(String bicCode) {
		this.bicCode = bicCode;
	}


	public BigDecimal getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(BigDecimal createuser) {
		this.createuser = createuser;
	}

	public String getIbanShort() {
		return this.ibanShort;
	}

	public void setIbanShort(String ibanShort) {
		this.ibanShort = ibanShort;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}



	public BigDecimal getLastupdateuser() {
		return this.lastupdateuser;
	}

	public void setLastupdateuser(BigDecimal lastupdateuser) {
		this.lastupdateuser = lastupdateuser;
	}

	public BigDecimal getMaxAccLength() {
		return this.maxAccLength;
	}

	public void setMaxAccLength(BigDecimal maxAccLength) {
		this.maxAccLength = maxAccLength;
	}

	public BigDecimal getMinAccLength() {
		return this.minAccLength;
	}

	public void setMinAccLength(BigDecimal minAccLength) {
		this.minAccLength = minAccLength;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getLastupdatedate() {
		return lastupdatedate;
	}

	public void setLastupdatedate(Date lastupdatedate) {
		this.lastupdatedate = lastupdatedate;
	}
}