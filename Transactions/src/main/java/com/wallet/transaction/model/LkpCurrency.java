package com.wallet.transaction.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the LKP_CURRENCY database table.
 * 
 */
@Entity
@Table(name="LKP_CURRENCY")
@NamedQuery(name="LkpCurrency.findAll", query="SELECT l FROM LkpCurrency l")
public class LkpCurrency implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_CURRENCY_CURRENCYID_GENERATOR", sequenceName="LKP_CURRENCY_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_CURRENCY_CURRENCYID_GENERATOR")
	@Column(name="CURRENCY_ID")
	private long currencyId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="CURRENCY_CODE")
	private String currencyCode;

	@Column(name="CURRENCY_DESCR")
	private String currencyDescr;

	private String dflt;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="STATUS_ID")
	private BigDecimal statusId;

	private BigDecimal updateindex;

	public LkpCurrency() {
	}

	public long getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(long currencyId) {
		this.currencyId = currencyId;
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

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyDescr() {
		return this.currencyDescr;
	}

	public void setCurrencyDescr(String currencyDescr) {
		this.currencyDescr = currencyDescr;
	}

	public String getDflt() {
		return this.dflt;
	}

	public void setDflt(String dflt) {
		this.dflt = dflt;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
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

	public BigDecimal getStatusId() {
		return this.statusId;
	}

	public void setStatusId(BigDecimal statusId) {
		this.statusId = statusId;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}