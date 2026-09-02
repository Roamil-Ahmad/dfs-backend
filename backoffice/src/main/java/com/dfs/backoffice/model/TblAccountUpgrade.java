package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_ACCOUNT_UPGRADE database table.
 * 
 */
@Entity
@Table(name="TBL_ACCOUNT_UPGRADE")
@NamedQuery(name="TblAccountUpgrade.findAll", query="SELECT t FROM TblAccountUpgrade t")
public class TblAccountUpgrade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_ACCOUNT_UPGRADE_ACCOUNTUPGRADEID_GENERATOR", sequenceName="TBL_ACCOUNT_UPGRADE_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_ACCOUNT_UPGRADE_ACCOUNTUPGRADEID_GENERATOR")
	@Column(name="ACCOUNT_UPGRADE_ID")
	private long accountUpgradeId;

	@Column(name="ACCOUNT_ID")
	private BigDecimal accountId;

	@Column(name="ACCOUNT_LEVEL_ID")
	private BigDecimal accountLevelId;

	@Column(name="ACCOUNT_PURPOSE_ID")
	private BigDecimal accountPurposeId;

	private String comments;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="FINGERPRINT_ID")
	private BigDecimal fingerprintId;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="OCCUPATION_ID")
	private BigDecimal occupationId;

	@Column(name="PROOF_OF_ADDRESS_ID")
	private BigDecimal proofOfAddressId;

	@Column(name="PROVINCE_ID")
	private BigDecimal provinceId;

	@Column(name="SELFIE_ID")
	private BigDecimal selfieId;

	@Column(name="SOURCE_OF_FUNDS_ID")
	private BigDecimal sourceOfFundsId;

	@Column(name="STATUS_ID")
	private BigDecimal statusId;

	@Column(name="NID_BACK_ID")
	private BigDecimal nidBackId;

	@Column(name="NID_FRONT_ID")
	private BigDecimal nidFrontId;

	private BigDecimal updateindex;

	public TblAccountUpgrade() {
	}

	public long getAccountUpgradeId() {
		return this.accountUpgradeId;
	}

	public void setAccountUpgradeId(long accountUpgradeId) {
		this.accountUpgradeId = accountUpgradeId;
	}

	public BigDecimal getAccountId() {
		return this.accountId;
	}

	public void setAccountId(BigDecimal accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getAccountLevelId() {
		return this.accountLevelId;
	}

	public void setAccountLevelId(BigDecimal accountLevelId) {
		this.accountLevelId = accountLevelId;
	}

	public BigDecimal getAccountPurposeId() {
		return this.accountPurposeId;
	}

	public void setAccountPurposeId(BigDecimal accountPurposeId) {
		this.accountPurposeId = accountPurposeId;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public BigDecimal getFingerprintId() {
		return this.fingerprintId;
	}

	public void setFingerprintId(BigDecimal fingerprintId) {
		this.fingerprintId = fingerprintId;
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

	public BigDecimal getOccupationId() {
		return this.occupationId;
	}

	public void setOccupationId(BigDecimal occupationId) {
		this.occupationId = occupationId;
	}

	public BigDecimal getProofOfAddressId() {
		return this.proofOfAddressId;
	}

	public void setProofOfAddressId(BigDecimal proofOfAddressId) {
		this.proofOfAddressId = proofOfAddressId;
	}

	public BigDecimal getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(BigDecimal provinceId) {
		this.provinceId = provinceId;
	}

	public BigDecimal getSelfieId() {
		return this.selfieId;
	}

	public void setSelfieId(BigDecimal selfieId) {
		this.selfieId = selfieId;
	}

	public BigDecimal getSourceOfFundsId() {
		return this.sourceOfFundsId;
	}

	public void setSourceOfFundsId(BigDecimal sourceOfFundsId) {
		this.sourceOfFundsId = sourceOfFundsId;
	}

	public BigDecimal getStatusId() {
		return this.statusId;
	}

	public void setStatusId(BigDecimal statusId) {
		this.statusId = statusId;
	}

	public BigDecimal getNidBackId() {
		return this.nidBackId;
	}

	public void setNidBackId(BigDecimal nidBackId) {
		this.nidBackId = nidBackId;
	}

	public BigDecimal getNidFrontId() {
		return this.nidFrontId;
	}

	public void setNidFrontId(BigDecimal nidFrontId) {
		this.nidFrontId = nidFrontId;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}