package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the LKP_ACCOUNT_STATUS database table.
 * 
 */
@Entity
@Table(name="LKP_ACCOUNT_STATUS")
@NamedQuery(name="LkpAccountStatus.findAll", query="SELECT l FROM LkpAccountStatus l")
public class LkpAccountStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_ACCOUNT_STATUS_ACCOUNTSTATUSID_GENERATOR", sequenceName="LKP_ACCOUNT_STATUS_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_ACCOUNT_STATUS_ACCOUNTSTATUSID_GENERATOR")
	@Column(name="ACCOUNT_STATUS_ID")
	private long accountStatusId;

	@Column(name="ACCOUNT_STATUS_CODE")
	private String accountStatusCode;

	@Column(name="ACCOUNT_STATUS_DESCR")
	private String accountStatusDescr;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="CREDIT_ALLOWED")
	private String creditAllowed;

	@Column(name="DEBIT_ALLOWED")
	private String debitAllowed;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="NFT_ALLOWED")
	private String nftAllowed;

	@Column(name="SORT_SEQ")
	private BigDecimal sortSeq;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	public long getAccountStatusId() {
		return this.accountStatusId;
	}

	public void setAccountStatusId(long accountStatusId) {
		this.accountStatusId = accountStatusId;
	}

	public String getAccountStatusCode() {
		return this.accountStatusCode;
	}

	public void setAccountStatusCode(String accountStatusCode) {
		this.accountStatusCode = accountStatusCode;
	}

	public String getAccountStatusDescr() {
		return this.accountStatusDescr;
	}

	public void setAccountStatusDescr(String accountStatusDescr) {
		this.accountStatusDescr = accountStatusDescr;
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

	public String getCreditAllowed() {
		return this.creditAllowed;
	}

	public void setCreditAllowed(String creditAllowed) {
		this.creditAllowed = creditAllowed;
	}

	public String getDebitAllowed() {
		return this.debitAllowed;
	}

	public void setDebitAllowed(String debitAllowed) {
		this.debitAllowed = debitAllowed;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Timestamp getLastupdatedate() {
		return this.lastupdatedate;
	}

	public void setLastupdatedate(Timestamp lastupdatedate) {
		this.lastupdatedate = lastupdatedate;
	}

	public BigDecimal getLastupdateuser() {
		return this.lastupdateuser;
	}

	public void setLastupdateuser(BigDecimal lastupdateuser) {
		this.lastupdateuser = lastupdateuser;
	}

	public String getNftAllowed() {
		return this.nftAllowed;
	}

	public void setNftAllowed(String nftAllowed) {
		this.nftAllowed = nftAllowed;
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

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

}