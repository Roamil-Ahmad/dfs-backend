package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the LKP_PROVINCE database table.
 * 
 */
@Entity
@Table(name="LKP_PROVINCE")
@NamedQuery(name="LkpProvince.findAll", query="SELECT l FROM LkpProvince l")
public class LkpProvince implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_PROVINCE_PROVINCEID_GENERATOR", sequenceName="LKP_PROVINCE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_PROVINCE_PROVINCEID_GENERATOR")
	@Column(name="PROVINCE_ID")
	private long provinceId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="PROVINCE_CODE")
	private String provinceCode;

	@Column(name="PROVINCE_DESCR")
	private String provinceDescr;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpBranch
	@OneToMany(mappedBy="lkpProvince")
	private List<LkpBranch> lkpBranches;

	//bi-directional many-to-one association to LkpDistrict
	@OneToMany(mappedBy="lkpProvince")
	private List<LkpDistrict> lkpDistricts;

	//bi-directional many-to-one association to LkpRegion
	@OneToMany(mappedBy="lkpProvince")
	private List<LkpRegion> lkpRegions;

	//bi-directional many-to-one association to TblAgent
	@OneToMany(mappedBy="lkpProvince")
	private List<TblAgent> tblAgents;

	//bi-directional many-to-one association to TblCustomer
	@OneToMany(mappedBy="lkpProvince")
	private List<TblCustomer> tblCustomers;

	//bi-directional many-to-one association to TblTaxDetail
	@OneToMany(mappedBy="lkpProvince")
	private List<TblTaxDetail> tblTaxDetails;

	public LkpProvince() {
	}

	public long getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
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

	public String getProvinceCode() {
		return this.provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceDescr() {
		return this.provinceDescr;
	}

	public void setProvinceDescr(String provinceDescr) {
		this.provinceDescr = provinceDescr;
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
		lkpBranch.setLkpProvince(this);

		return lkpBranch;
	}

	public LkpBranch removeLkpBranch(LkpBranch lkpBranch) {
		getLkpBranches().remove(lkpBranch);
		lkpBranch.setLkpProvince(null);

		return lkpBranch;
	}

	public List<LkpDistrict> getLkpDistricts() {
		return this.lkpDistricts;
	}

	public void setLkpDistricts(List<LkpDistrict> lkpDistricts) {
		this.lkpDistricts = lkpDistricts;
	}

	public LkpDistrict addLkpDistrict(LkpDistrict lkpDistrict) {
		getLkpDistricts().add(lkpDistrict);
		lkpDistrict.setLkpProvince(this);

		return lkpDistrict;
	}

	public LkpDistrict removeLkpDistrict(LkpDistrict lkpDistrict) {
		getLkpDistricts().remove(lkpDistrict);
		lkpDistrict.setLkpProvince(null);

		return lkpDistrict;
	}

	public List<LkpRegion> getLkpRegions() {
		return this.lkpRegions;
	}

	public void setLkpRegions(List<LkpRegion> lkpRegions) {
		this.lkpRegions = lkpRegions;
	}

	public LkpRegion addLkpRegion(LkpRegion lkpRegion) {
		getLkpRegions().add(lkpRegion);
		lkpRegion.setLkpProvince(this);

		return lkpRegion;
	}

	public LkpRegion removeLkpRegion(LkpRegion lkpRegion) {
		getLkpRegions().remove(lkpRegion);
		lkpRegion.setLkpProvince(null);

		return lkpRegion;
	}

	public List<TblAgent> getTblAgents() {
		return this.tblAgents;
	}

	public void setTblAgents(List<TblAgent> tblAgents) {
		this.tblAgents = tblAgents;
	}

	public TblAgent addTblAgent(TblAgent tblAgent) {
		getTblAgents().add(tblAgent);
		tblAgent.setLkpProvince(this);

		return tblAgent;
	}

	public TblAgent removeTblAgent(TblAgent tblAgent) {
		getTblAgents().remove(tblAgent);
		tblAgent.setLkpProvince(null);

		return tblAgent;
	}

	public List<TblCustomer> getTblCustomers() {
		return this.tblCustomers;
	}

	public void setTblCustomers(List<TblCustomer> tblCustomers) {
		this.tblCustomers = tblCustomers;
	}

	public TblCustomer addTblCustomer(TblCustomer tblCustomer) {
		getTblCustomers().add(tblCustomer);
		tblCustomer.setLkpProvince(this);

		return tblCustomer;
	}

	public TblCustomer removeTblCustomer(TblCustomer tblCustomer) {
		getTblCustomers().remove(tblCustomer);
		tblCustomer.setLkpProvince(null);

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
		tblTaxDetail.setLkpProvince(this);

		return tblTaxDetail;
	}

	public TblTaxDetail removeTblTaxDetail(TblTaxDetail tblTaxDetail) {
		getTblTaxDetails().remove(tblTaxDetail);
		tblTaxDetail.setLkpProvince(null);

		return tblTaxDetail;
	}

}