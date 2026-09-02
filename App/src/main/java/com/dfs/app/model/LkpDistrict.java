package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the LKP_DISTRICT database table.
 * 
 */
@Entity
@Table(name="LKP_DISTRICT")
@NamedQuery(name="LkpDistrict.findAll", query="SELECT l FROM LkpDistrict l")
public class LkpDistrict implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_DISTRICT_DISTRICTID_GENERATOR", sequenceName="LKP_DISTRICT_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_DISTRICT_DISTRICTID_GENERATOR")
	@Column(name="DISTRICT_ID")
	private long districtId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DISTRICT_CODE")
	private String districtCode;

	@Column(name="DISTRICT_DESCR")
	private String districtDescr;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpCity
	@OneToMany(mappedBy="lkpDistrict")
	private List<LkpCity> lkpCities;

	//bi-directional many-to-one association to LkpProvince
	@ManyToOne
	@JoinColumn(name="PROVINCE_ID")
	private LkpProvince lkpProvince;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;



	//bi-directional many-to-one association to TblCustomer
	@OneToMany(mappedBy="lkpDistrict")
	private List<TblCustomer> tblCustomers;

	public LkpDistrict() {
	}

	public long getDistrictId() {
		return this.districtId;
	}

	public void setDistrictId(long districtId) {
		this.districtId = districtId;
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

	public String getDistrictCode() {
		return this.districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictDescr() {
		return this.districtDescr;
	}

	public void setDistrictDescr(String districtDescr) {
		this.districtDescr = districtDescr;
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

	public List<LkpCity> getLkpCities() {
		return this.lkpCities;
	}

	public void setLkpCities(List<LkpCity> lkpCities) {
		this.lkpCities = lkpCities;
	}

	public LkpCity addLkpCity(LkpCity lkpCity) {
		getLkpCities().add(lkpCity);
		lkpCity.setLkpDistrict(this);

		return lkpCity;
	}

	public LkpCity removeLkpCity(LkpCity lkpCity) {
		getLkpCities().remove(lkpCity);
		lkpCity.setLkpDistrict(null);

		return lkpCity;
	}

	public LkpProvince getLkpProvince() {
		return this.lkpProvince;
	}

	public void setLkpProvince(LkpProvince lkpProvince) {
		this.lkpProvince = lkpProvince;
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
		tblCustomer.setLkpDistrict(this);

		return tblCustomer;
	}

	public TblCustomer removeTblCustomer(TblCustomer tblCustomer) {
		getTblCustomers().remove(tblCustomer);
		tblCustomer.setLkpDistrict(null);

		return tblCustomer;
	}

}