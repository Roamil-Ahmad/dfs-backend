package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the LKP_TAX_TYPE database table.
 * 
 */
@Entity
@Table(name="LKP_TAX_TYPE")
@NamedQuery(name="LkpTaxType.findAll", query="SELECT l FROM LkpTaxType l")
public class LkpTaxType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_TAX_TYPE_TAXTYPEID_GENERATOR", sequenceName="LKP_TAX_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_TAX_TYPE_TAXTYPEID_GENERATOR")
	@Column(name="TAX_TYPE_ID")
	private long taxTypeId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="TAX_TYPE_CODE")
	private String taxTypeCode;

	@Column(name="TAX_TYPE_DESCR")
	private String taxTypeDescr;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblTaxRegime
	@OneToMany(mappedBy="lkpTaxType")
	private List<TblTaxRegime> tblTaxRegimes;

	public LkpTaxType() {
	}

	public long getTaxTypeId() {
		return this.taxTypeId;
	}

	public void setTaxTypeId(long taxTypeId) {
		this.taxTypeId = taxTypeId;
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

	public String getTaxTypeCode() {
		return this.taxTypeCode;
	}

	public void setTaxTypeCode(String taxTypeCode) {
		this.taxTypeCode = taxTypeCode;
	}

	public String getTaxTypeDescr() {
		return this.taxTypeDescr;
	}

	public void setTaxTypeDescr(String taxTypeDescr) {
		this.taxTypeDescr = taxTypeDescr;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

	public List<TblTaxRegime> getTblTaxRegimes() {
		return this.tblTaxRegimes;
	}

	public void setTblTaxRegimes(List<TblTaxRegime> tblTaxRegimes) {
		this.tblTaxRegimes = tblTaxRegimes;
	}

	public TblTaxRegime addTblTaxRegime(TblTaxRegime tblTaxRegime) {
		getTblTaxRegimes().add(tblTaxRegime);
		tblTaxRegime.setLkpTaxType(this);

		return tblTaxRegime;
	}

	public TblTaxRegime removeTblTaxRegime(TblTaxRegime tblTaxRegime) {
		getTblTaxRegimes().remove(tblTaxRegime);
		tblTaxRegime.setLkpTaxType(null);

		return tblTaxRegime;
	}

}