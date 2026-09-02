package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the LKP_SOURCE_OF_INCOME database table.
 * 
 */
@Entity
@Table(name="LKP_SOURCE_OF_INCOME")
@NamedQuery(name="LkpSourceOfIncome.findAll", query="SELECT l FROM LkpSourceOfIncome l")
public class LkpSourceOfIncome implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_SOURCE_OF_INCOME_SOURCEOFINCOMEID_GENERATOR", sequenceName="LKP_SOURCE_OF_INCOME_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_SOURCE_OF_INCOME_SOURCEOFINCOMEID_GENERATOR")
	@Column(name="SOURCE_OF_INCOME_ID")
	private long sourceOfIncomeId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="SOURCE_OF_INCOME_CODE")
	private String sourceOfIncomeCode;

	@Column(name="SOURCE_OF_INCOME_DESCR")
	private String sourceOfIncomeDescr;

	@Column(name="SOURCE_OF_INCOME_NAME")
	private String sourceOfIncomeName;

	private BigDecimal updateindex;

	public LkpSourceOfIncome() {
	}

	public long getSourceOfIncomeId() {
		return this.sourceOfIncomeId;
	}

	public void setSourceOfIncomeId(long sourceOfIncomeId) {
		this.sourceOfIncomeId = sourceOfIncomeId;
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

	public String getSourceOfIncomeCode() {
		return this.sourceOfIncomeCode;
	}

	public void setSourceOfIncomeCode(String sourceOfIncomeCode) {
		this.sourceOfIncomeCode = sourceOfIncomeCode;
	}

	public String getSourceOfIncomeDescr() {
		return this.sourceOfIncomeDescr;
	}

	public void setSourceOfIncomeDescr(String sourceOfIncomeDescr) {
		this.sourceOfIncomeDescr = sourceOfIncomeDescr;
	}

	public String getSourceOfIncomeName() {
		return this.sourceOfIncomeName;
	}

	public void setSourceOfIncomeName(String sourceOfIncomeName) {
		this.sourceOfIncomeName = sourceOfIncomeName;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}