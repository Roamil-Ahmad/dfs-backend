package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the TBL_TAX_REGIME database table.
 * 
 */
@Entity
@Table(name="TBL_TAX_REGIME")
@NamedQuery(name="TblTaxRegime.findAll", query="SELECT t FROM TblTaxRegime t")
public class TblTaxRegime implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TAX_REGIME_TAXREGIMEID_GENERATOR", sequenceName="TBL_TAX_REGIME_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TAX_REGIME_TAXREGIMEID_GENERATOR")
	@Column(name="TAX_REGIME_ID")
	private long taxRegimeId;

	@Column(name="APPLICABLE_ON")
	private String applicableOn;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_FROM")
	private Date effectiveFrom;

	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_TO")
	private Date effectiveTo;

	@Column(name="IS_ACTIVE")
	private String isActive;


	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="TAX_REGIME_CODE")
	private String taxRegimeCode;

	@Column(name="TAX_REGIME_DESCR")
	private String taxRegimeDescr;

	private BigDecimal updateindex;


	public long getTaxRegimeId() {
		return this.taxRegimeId;
	}

	public void setTaxRegimeId(long taxRegimeId) {
		this.taxRegimeId = taxRegimeId;
	}

	public String getApplicableOn() {
		return this.applicableOn;
	}

	public void setApplicableOn(String applicableOn) {
		this.applicableOn = applicableOn;
	}

	public Timestamp getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Timestamp createdate) {
		this.createdate = createdate;
	}

	public BigDecimal getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(BigDecimal createuser) {
		this.createuser = createuser;
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

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}


	public Timestamp getLastupdatedate() {
		return this.lastupdatedate;
	}

	public void setLastupdatedate(Timestamp lastupdatedate) {
		this.lastupdatedate = lastupdatedate;
	}

	public BigDecimal getLastupdateuser() {
		return this.lastupdateuser;
	}

	public void setLastupdateuser(BigDecimal lastupdateuser) {
		this.lastupdateuser = lastupdateuser;
	}

	public String getTaxRegimeCode() {
		return this.taxRegimeCode;
	}

	public void setTaxRegimeCode(String taxRegimeCode) {
		this.taxRegimeCode = taxRegimeCode;
	}

	public String getTaxRegimeDescr() {
		return this.taxRegimeDescr;
	}

	public void setTaxRegimeDescr(String taxRegimeDescr) {
		this.taxRegimeDescr = taxRegimeDescr;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}
}