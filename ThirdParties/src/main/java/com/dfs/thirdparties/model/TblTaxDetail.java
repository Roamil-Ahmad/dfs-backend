package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_TAX_DETAIL database table.
 * 
 */
@Entity
@Table(name="TBL_TAX_DETAIL")
@NamedQuery(name="TblTaxDetail.findAll", query="SELECT t FROM TblTaxDetail t")
public class TblTaxDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TAX_DETAIL_TAXDETAILID_GENERATOR", sequenceName="TBL_TAX_DETAIL_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TAX_DETAIL_TAXDETAILID_GENERATOR")
	@Column(name="TAX_DETAIL_ID")
	private long taxDetailId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="TAX_EXEMPT")
	private String taxExempt;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpCity
	@ManyToOne
	@JoinColumn(name="CITY_ID")
	private LkpCity lkpCity;

	//bi-directional many-to-one association to LkpProvince
	@ManyToOne
	@JoinColumn(name="PROVINCE_ID")
	private LkpProvince lkpProvince;

	//bi-directional many-to-one association to TblTaxRegime
	@ManyToOne
	@JoinColumn(name="TAX_REGIME_ID")
	private TblTaxRegime tblTaxRegime;

	public TblTaxDetail() {
	}

	public long getTaxDetailId() {
		return this.taxDetailId;
	}

	public void setTaxDetailId(long taxDetailId) {
		this.taxDetailId = taxDetailId;
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

	public String getTaxExempt() {
		return this.taxExempt;
	}

	public void setTaxExempt(String taxExempt) {
		this.taxExempt = taxExempt;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public LkpCity getLkpCity() {
		return this.lkpCity;
	}

	public void setLkpCity(LkpCity lkpCity) {
		this.lkpCity = lkpCity;
	}

	public LkpProvince getLkpProvince() {
		return this.lkpProvince;
	}

	public void setLkpProvince(LkpProvince lkpProvince) {
		this.lkpProvince = lkpProvince;
	}

	public TblTaxRegime getTblTaxRegime() {
		return this.tblTaxRegime;
	}

	public void setTblTaxRegime(TblTaxRegime tblTaxRegime) {
		this.tblTaxRegime = tblTaxRegime;
	}

}