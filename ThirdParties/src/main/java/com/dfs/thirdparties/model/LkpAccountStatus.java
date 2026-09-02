package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
	@SequenceGenerator(name="LKP_ACCOUNT_STATUS_ACCOUNTSTATUSID_GENERATOR", sequenceName="LKP_ACCOUNT_STATUS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_ACCOUNT_STATUS_ACCOUNTSTATUSID_GENERATOR")
	@Column(name="ACCOUNT_STATUS_ID")
	private long accountStatusId;

	@Column(name="ACCOUNT_STATUS_CODE")
	private String accountStatusCode;

	@Column(name="ACCOUNT_STATUS_DESCR")
	private String accountStatusDescr;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="CREDIT_ALLOWED")
	private String creditAllowed;

	@Column(name="DEBIT_ALLOWED")
	private String debitAllowed;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

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

	//bi-directional many-to-one association to TblAccount
	@OneToMany(mappedBy="lkpAccountStatus")
	private List<TblAccount> tblAccounts;

	//bi-directional many-to-one association to TblGlAccount
	@OneToMany(mappedBy="lkpAccountStatus")
	private List<TblGlAccount> tblGlAccounts;

	public LkpAccountStatus() {
	}

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

	public List<TblAccount> getTblAccounts() {
		return this.tblAccounts;
	}

	public void setTblAccounts(List<TblAccount> tblAccounts) {
		this.tblAccounts = tblAccounts;
	}

	public TblAccount addTblAccount(TblAccount tblAccount) {
		getTblAccounts().add(tblAccount);
		tblAccount.setLkpAccountStatus(this);

		return tblAccount;
	}

	public TblAccount removeTblAccount(TblAccount tblAccount) {
		getTblAccounts().remove(tblAccount);
		tblAccount.setLkpAccountStatus(null);

		return tblAccount;
	}

	public List<TblGlAccount> getTblGlAccounts() {
		return this.tblGlAccounts;
	}

	public void setTblGlAccounts(List<TblGlAccount> tblGlAccounts) {
		this.tblGlAccounts = tblGlAccounts;
	}

	public TblGlAccount addTblGlAccount(TblGlAccount tblGlAccount) {
		getTblGlAccounts().add(tblGlAccount);
		tblGlAccount.setLkpAccountStatus(this);

		return tblGlAccount;
	}

	public TblGlAccount removeTblGlAccount(TblGlAccount tblGlAccount) {
		getTblGlAccounts().remove(tblGlAccount);
		tblGlAccount.setLkpAccountStatus(null);

		return tblGlAccount;
	}

}