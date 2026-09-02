package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the LKP_REGION database table.
 * 
 */
@Entity
@Table(name="LKP_REGION")
@NamedQuery(name="LkpRegion.findAll", query="SELECT l FROM LkpRegion l")
public class LkpRegion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_REGION_REGIONID_GENERATOR", sequenceName="LKP_REGION_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_REGION_REGIONID_GENERATOR")
	@Column(name="REGION_ID")
	private long regionId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="REGION_CODE")
	private String regionCode;

	@Column(name="REGION_DESCR")
	private String regionDescr;

	@Column(name="SORT_SEQ")
	private BigDecimal sortSeq;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpBranch
	@OneToMany(mappedBy="lkpRegion")
	private List<LkpBranch> lkpBranches;

	//bi-directional many-to-one association to LkpProvince
	@ManyToOne
	@JoinColumn(name="PROVINCE_ID")
	private LkpProvince lkpProvince;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	public LkpRegion() {
	}

	public long getRegionId() {
		return this.regionId;
	}

	public void setRegionId(long regionId) {
		this.regionId = regionId;
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

	public String getRegionCode() {
		return this.regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getRegionDescr() {
		return this.regionDescr;
	}

	public void setRegionDescr(String regionDescr) {
		this.regionDescr = regionDescr;
	}

	public BigDecimal getSortSeq() {
		return this.sortSeq;
	}

	public void setSortSeq(BigDecimal sortSeq) {
		this.sortSeq = sortSeq;
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
		lkpBranch.setLkpRegion(this);

		return lkpBranch;
	}

	public LkpBranch removeLkpBranch(LkpBranch lkpBranch) {
		getLkpBranches().remove(lkpBranch);
		lkpBranch.setLkpRegion(null);

		return lkpBranch;
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

}