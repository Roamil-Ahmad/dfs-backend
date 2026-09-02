package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the LKP_CITY database table.
 * 
 */
@Entity
@Table(name="LKP_CITY")
@NamedQuery(name="LkpCity.findAll", query="SELECT l FROM LkpCity l")
public class LkpCity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_CITY_CITYID_GENERATOR", sequenceName="LKP_CITY_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_CITY_CITYID_GENERATOR")
	@Column(name="CITY_ID")
	private long cityId;

	@Column(name="CITY_CODE")
	private String cityCode;

	@Column(name="CITY_DESCR")
	private String cityDescr;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpBranch
	@OneToMany(mappedBy="lkpCity")
	private List<LkpBranch> lkpBranches;

	//bi-directional many-to-one association to LkpCountry
	@ManyToOne
	@JoinColumn(name="COUNTRY_ID")
	private LkpCountry lkpCountry;

	//bi-directional many-to-one association to LkpDistrict
	@ManyToOne
	@JoinColumn(name="DISTRICT_ID")
	private LkpDistrict lkpDistrict;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;



	//bi-directional many-to-one association to TblCustomer
	@OneToMany(mappedBy="lkpCity")
	private List<TblCustomer> tblCustomers;

	//bi-directional many-to-one association to TblTaxDetail
	@OneToMany(mappedBy="lkpCity")
	private List<TblTaxDetail> tblTaxDetails;

	public LkpCity() {
	}

	public long getCityId() {
		return this.cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityDescr() {
		return this.cityDescr;
	}

	public void setCityDescr(String cityDescr) {
		this.cityDescr = cityDescr;
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

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public List<LkpBranch> getLkpBranches() {
		return this.lkpBranches;
	}

	public void setLkpBranches(List<LkpBranch> lkpBranches) {
		this.lkpBranches = lkpBranches;
	}

	public LkpBranch addLkpBranch(LkpBranch lkpBranch) {
		getLkpBranches().add(lkpBranch);
		lkpBranch.setLkpCity(this);

		return lkpBranch;
	}

	public LkpBranch removeLkpBranch(LkpBranch lkpBranch) {
		getLkpBranches().remove(lkpBranch);
		lkpBranch.setLkpCity(null);

		return lkpBranch;
	}

	public LkpCountry getLkpCountry() {
		return this.lkpCountry;
	}

	public void setLkpCountry(LkpCountry lkpCountry) {
		this.lkpCountry = lkpCountry;
	}

	public LkpDistrict getLkpDistrict() {
		return this.lkpDistrict;
	}

	public void setLkpDistrict(LkpDistrict lkpDistrict) {
		this.lkpDistrict = lkpDistrict;
	}

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}



	public List<TblCustomer> getTblCustomers() {
		return this.tblCustomers;
	}

	public void setTblCustomers(List<TblCustomer> tblCustomers) {
		this.tblCustomers = tblCustomers;
	}

	public TblCustomer addTblCustomer(TblCustomer tblCustomer) {
		getTblCustomers().add(tblCustomer);
		tblCustomer.setLkpCity(this);

		return tblCustomer;
	}

	public TblCustomer removeTblCustomer(TblCustomer tblCustomer) {
		getTblCustomers().remove(tblCustomer);
		tblCustomer.setLkpCity(null);

		return tblCustomer;
	}

	public List<TblTaxDetail> getTblTaxDetails() {
		return this.tblTaxDetails;
	}

	public void setTblTaxDetails(List<TblTaxDetail> tblTaxDetails) {
		this.tblTaxDetails = tblTaxDetails;
	}

	public TblTaxDetail addTblTaxDetail(TblTaxDetail tblTaxDetail) {
		getTblTaxDetails().add(tblTaxDetail);
		tblTaxDetail.setLkpCity(this);

		return tblTaxDetail;
	}

	public TblTaxDetail removeTblTaxDetail(TblTaxDetail tblTaxDetail) {
		getTblTaxDetails().remove(tblTaxDetail);
		tblTaxDetail.setLkpCity(null);

		return tblTaxDetail;
	}

}