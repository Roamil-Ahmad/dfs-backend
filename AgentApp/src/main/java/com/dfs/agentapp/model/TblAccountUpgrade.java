package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


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

	private String comments;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;


	//bi-directional many-to-one association to LkpSourceOfFund
	@ManyToOne
	@JoinColumn(name="SOURCE_OF_FUNDS_ID")
	private LkpSourceOfIncome lkpSourceOfIncome;



	@ManyToOne
	@JoinColumn(name="PROVINCE_ID")
	private LkpProvince lkpProvince;

	@ManyToOne
	@JoinColumn(name="OCCUPATION_ID")
	private LkpOccupation lkpOccupation;

	@ManyToOne
	@JoinColumn(name="ACCOUNT_PURPOSE_ID")
	private LkpAccountPurpose lkpAccountPurpose;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblAccount
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TblAccount tblAccount;

	//bi-directional many-to-one association to TblAccountLevel
	@ManyToOne
	@JoinColumn(name="ACCOUNT_LEVEL_ID")
	private TblAccountLevel tblAccountLevel;

	//bi-directional many-to-one association to TblDocument
	@ManyToOne
	@JoinColumn(name="NID_BACK_ID")
	private TblDocument tblDocument1;

	//bi-directional many-to-one association to TblDocument
	@ManyToOne
	@JoinColumn(name="SELFIE_ID")
	private TblDocument tblDocument2;

	//bi-directional many-to-one association to TblDocument
	@ManyToOne
	@JoinColumn(name="PROOF_OF_ADDRESS_ID")
	private TblDocument tblDocument3;

	//bi-directional many-to-one association to TblDocument
	@ManyToOne
	@JoinColumn(name="NID_FRONT_ID")
	private TblDocument tblDocument4;

	//bi-directional many-to-one association to TblDocument
	@ManyToOne
	@JoinColumn(name="FINGERPRINT_ID")
	private TblDocument tblDocument5;

	public TblAccountUpgrade() {
	}

	public long getAccountUpgradeId() {
		return this.accountUpgradeId;
	}

	public void setAccountUpgradeId(long accountUpgradeId) {
		this.accountUpgradeId = accountUpgradeId;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public LkpSourceOfIncome getLkpSourceOfIncome() {
		return lkpSourceOfIncome;
	}

	public void setLkpSourceOfIncome(LkpSourceOfIncome lkpSourceOfIncome) {
		this.lkpSourceOfIncome = lkpSourceOfIncome;
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

	public TblAccount getTblAccount() {
		return this.tblAccount;
	}

	public void setTblAccount(TblAccount tblAccount) {
		this.tblAccount = tblAccount;
	}

	public TblAccountLevel getTblAccountLevel() {
		return this.tblAccountLevel;
	}

	public void setTblAccountLevel(TblAccountLevel tblAccountLevel) {
		this.tblAccountLevel = tblAccountLevel;
	}

	public TblDocument getTblDocument1() {
		return this.tblDocument1;
	}

	public void setTblDocument1(TblDocument tblDocument1) {
		this.tblDocument1 = tblDocument1;
	}

	public TblDocument getTblDocument2() {
		return this.tblDocument2;
	}

	public void setTblDocument2(TblDocument tblDocument2) {
		this.tblDocument2 = tblDocument2;
	}

	public TblDocument getTblDocument3() {
		return this.tblDocument3;
	}

	public void setTblDocument3(TblDocument tblDocument3) {
		this.tblDocument3 = tblDocument3;
	}

	public TblDocument getTblDocument4() {
		return this.tblDocument4;
	}

	public void setTblDocument4(TblDocument tblDocument4) {
		this.tblDocument4 = tblDocument4;
	}

	public TblDocument getTblDocument5() {
		return this.tblDocument5;
	}

	public void setTblDocument5(TblDocument tblDocument5) {
		this.tblDocument5 = tblDocument5;
	}

	public LkpProvince getLkpProvince() {
		return lkpProvince;
	}

	public void setLkpProvince(LkpProvince lkpProvince) {
		this.lkpProvince = lkpProvince;
	}

	public LkpOccupation getLkpOccupation() {
		return lkpOccupation;
	}

	public void setLkpOccupation(LkpOccupation lkpOccupation) {
		this.lkpOccupation = lkpOccupation;
	}

	public LkpAccountPurpose getLkpAccountPurpose() {
		return lkpAccountPurpose;
	}

	public void setLkpAccountPurpose(LkpAccountPurpose lkpAccountPurpose) {
		this.lkpAccountPurpose = lkpAccountPurpose;
	}
}