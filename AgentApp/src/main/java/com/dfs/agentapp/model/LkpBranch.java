package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the LKP_BRANCH database table.
 * 
 */
@Entity
@Table(name="LKP_BRANCH")
@NamedQuery(name="LkpBranch.findAll", query="SELECT l FROM LkpBranch l")
public class LkpBranch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_BRANCH_BRANCHID_GENERATOR", sequenceName="LKP_BRANCH_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_BRANCH_BRANCHID_GENERATOR")
	@Column(name="BRANCH_ID")
	private long branchId;

	private String address1;

	private String address2;

	private String area;

	private String atm;

	@Column(name="BRANCH_CODE")
	private String branchCode;

	@Column(name="BRANCH_DESCR")
	private String branchDescr;

	@Column(name="BRANCH_GROUP")
	private String branchGroup;

	private Date createdate;

	private BigDecimal createuser;

	private String email;

	@Column(name="HIGH_RISK")
	private String highRisk;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private String landline;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private String mobile;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpCity
	@ManyToOne
	@JoinColumn(name="CITY_ID")
	private LkpCity lkpCity;

	//bi-directional many-to-one association to LkpProvince
	@ManyToOne
	@JoinColumn(name="PROVINCE_ID")
	private LkpProvince lkpProvince;

	//bi-directional many-to-one association to LkpRegion
	@ManyToOne
	@JoinColumn(name="REGION_ID")
	private LkpRegion lkpRegion;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblAccountDebitCard
	@OneToMany(mappedBy="lkpBranch")
	private List<TblAccountDebitCard> tblAccountDebitCards;

	//bi-directional many-to-one association to TblDebitCardRequest
	@OneToMany(mappedBy="lkpBranch")
	private List<TblDebitCardRequest> tblDebitCardRequests;

	//bi-directional many-to-one association to TblTransHead
	@OneToMany(mappedBy="lkpBranch")
	private List<TblTransHead> tblTransHeads;

	public LkpBranch() {
	}

	public long getBranchId() {
		return this.branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAtm() {
		return this.atm;
	}

	public void setAtm(String atm) {
		this.atm = atm;
	}

	public String getBranchCode() {
		return this.branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchDescr() {
		return this.branchDescr;
	}

	public void setBranchDescr(String branchDescr) {
		this.branchDescr = branchDescr;
	}

	public String getBranchGroup() {
		return this.branchGroup;
	}

	public void setBranchGroup(String branchGroup) {
		this.branchGroup = branchGroup;
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHighRisk() {
		return this.highRisk;
	}

	public void setHighRisk(String highRisk) {
		this.highRisk = highRisk;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getLandline() {
		return this.landline;
	}

	public void setLandline(String landline) {
		this.landline = landline;
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

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public LkpRegion getLkpRegion() {
		return this.lkpRegion;
	}

	public void setLkpRegion(LkpRegion lkpRegion) {
		this.lkpRegion = lkpRegion;
	}

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

	public List<TblAccountDebitCard> getTblAccountDebitCards() {
		return this.tblAccountDebitCards;
	}

	public void setTblAccountDebitCards(List<TblAccountDebitCard> tblAccountDebitCards) {
		this.tblAccountDebitCards = tblAccountDebitCards;
	}

	public TblAccountDebitCard addTblAccountDebitCard(TblAccountDebitCard tblAccountDebitCard) {
		getTblAccountDebitCards().add(tblAccountDebitCard);
		tblAccountDebitCard.setLkpBranch(this);

		return tblAccountDebitCard;
	}

	public TblAccountDebitCard removeTblAccountDebitCard(TblAccountDebitCard tblAccountDebitCard) {
		getTblAccountDebitCards().remove(tblAccountDebitCard);
		tblAccountDebitCard.setLkpBranch(null);

		return tblAccountDebitCard;
	}

	public List<TblDebitCardRequest> getTblDebitCardRequests() {
		return this.tblDebitCardRequests;
	}

	public void setTblDebitCardRequests(List<TblDebitCardRequest> tblDebitCardRequests) {
		this.tblDebitCardRequests = tblDebitCardRequests;
	}

	public TblDebitCardRequest addTblDebitCardRequest(TblDebitCardRequest tblDebitCardRequest) {
		getTblDebitCardRequests().add(tblDebitCardRequest);
		tblDebitCardRequest.setLkpBranch(this);

		return tblDebitCardRequest;
	}

	public TblDebitCardRequest removeTblDebitCardRequest(TblDebitCardRequest tblDebitCardRequest) {
		getTblDebitCardRequests().remove(tblDebitCardRequest);
		tblDebitCardRequest.setLkpBranch(null);

		return tblDebitCardRequest;
	}

	public List<TblTransHead> getTblTransHeads() {
		return this.tblTransHeads;
	}

	public void setTblTransHeads(List<TblTransHead> tblTransHeads) {
		this.tblTransHeads = tblTransHeads;
	}

	public TblTransHead addTblTransHead(TblTransHead tblTransHead) {
		getTblTransHeads().add(tblTransHead);
		tblTransHead.setLkpBranch(this);

		return tblTransHead;
	}

	public TblTransHead removeTblTransHead(TblTransHead tblTransHead) {
		getTblTransHeads().remove(tblTransHead);
		tblTransHead.setLkpBranch(null);

		return tblTransHead;
	}

}