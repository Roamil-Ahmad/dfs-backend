package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_TAX_RATE database table.
 * 
 */
@Entity
@Table(name="TBL_TAX_RATE")
@NamedQuery(name="TblTaxRate.findAll", query="SELECT t FROM TblTaxRate t")
public class TblTaxRate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TAX_RATE_TAXRATEID_GENERATOR", sequenceName="TBL_TAX_RATE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TAX_RATE_TAXRATEID_GENERATOR")
	@Column(name="TAX_RATE_ID")
	private long taxRateId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="FROM_AMOUNT")
	private BigDecimal fromAmount;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="TAX_RATE")
	private BigDecimal taxRate;

	@Column(name="TAX_RATE_TYPE")
	private String taxRateType;

	@Column(name="THRESHOLD_AMOUNT")
	private BigDecimal thresholdAmount;

	@Column(name="TO_AMOUNT")
	private BigDecimal toAmount;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblTaxRegime
	@ManyToOne
	@JoinColumn(name="TAX_REGIME_ID")
	private TblTaxRegime tblTaxRegime;

	public TblTaxRate() {
	}

	public long getTaxRateId() {
		return this.taxRateId;
	}

	public void setTaxRateId(long taxRateId) {
		this.taxRateId = taxRateId;
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

	public BigDecimal getFromAmount() {
		return this.fromAmount;
	}

	public void setFromAmount(BigDecimal fromAmount) {
		this.fromAmount = fromAmount;
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

	public BigDecimal getTaxRate() {
		return this.taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getTaxRateType() {
		return this.taxRateType;
	}

	public void setTaxRateType(String taxRateType) {
		this.taxRateType = taxRateType;
	}

	public BigDecimal getThresholdAmount() {
		return this.thresholdAmount;
	}

	public void setThresholdAmount(BigDecimal thresholdAmount) {
		this.thresholdAmount = thresholdAmount;
	}

	public BigDecimal getToAmount() {
		return this.toAmount;
	}

	public void setToAmount(BigDecimal toAmount) {
		this.toAmount = toAmount;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public TblTaxRegime getTblTaxRegime() {
		return this.tblTaxRegime;
	}

	public void setTblTaxRegime(TblTaxRegime tblTaxRegime) {
		this.tblTaxRegime = tblTaxRegime;
	}

}