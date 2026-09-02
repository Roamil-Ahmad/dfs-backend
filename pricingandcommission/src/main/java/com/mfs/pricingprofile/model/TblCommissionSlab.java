package com.mfs.pricingprofile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_COMMISSION_SLAB database table.
 * 
 */
@Entity
@Table(name="TBL_COMMISSION_SLAB")
@NamedQuery(name="TblCommissionSlab.findAll", query="SELECT t FROM TblCommissionSlab t")
public class TblCommissionSlab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_COMMISSION_SLAB_COMMISSIONSLABID_GENERATOR", sequenceName="TBL_COMMISSION_SLAB_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_COMMISSION_SLAB_COMMISSIONSLABID_GENERATOR")
	@Column(name="COMMISSION_SLAB_ID")
	private long commissionSlabId;

	@Column(name="COMMISSION_AMOUNT")
	private BigDecimal commissionAmount;

	@Column(name="COMMISSION_PERCENTAGE")
	private BigDecimal commissionPercentage;

	@Column(name="COMMISSION_TYPE")
	private String commissionType;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Column(name="TOAMOUNT")
	private BigDecimal toAmount;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="FROMAMOUNT")
	private BigDecimal fromAmount;

	private String isActive;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblCommissionProfile
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="COMMISSION_PROFILE_ID")
	private TblCommissionProfile tblCommissionProfile;

	public long getCommissionSlabId() {
		return this.commissionSlabId;
	}

	public void setCommissionSlabId(long commissionSlabId) {
		this.commissionSlabId = commissionSlabId;
	}

	public BigDecimal getCommissionAmount() {
		return this.commissionAmount;
	}

	public void setCommissionAmount(BigDecimal commissionAmount) {
		this.commissionAmount = commissionAmount;
	}

	public BigDecimal getCommissionPercentage() {
		return this.commissionPercentage;
	}

	public void setCommissionPercentage(BigDecimal commissionPercentage) {
		this.commissionPercentage = commissionPercentage;
	}

	public String getCommissionType() {
		return this.commissionType;
	}

	public void setCommissionType(String commissionType) {
		this.commissionType = commissionType;
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

	public BigDecimal getToAmount() {
		return toAmount;
	}

	public void setToAmount(BigDecimal toAmount) {
		this.toAmount = toAmount;
	}

	public BigDecimal getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(BigDecimal fromAmount) {
		this.fromAmount = fromAmount;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public TblCommissionProfile getTblCommissionProfile() {
		return this.tblCommissionProfile;
	}

	public void setTblCommissionProfile(TblCommissionProfile tblCommissionProfile) {
		this.tblCommissionProfile = tblCommissionProfile;
	}

}