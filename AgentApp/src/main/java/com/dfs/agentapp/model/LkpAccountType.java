package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the LKP_ACCOUNT_TYPE database table.
 * 
 */
@Entity
@Table(name="LKP_ACCOUNT_TYPE")
@NamedQuery(name="LkpAccountType.findAll", query="SELECT l FROM LkpAccountType l")
public class LkpAccountType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_ACCOUNT_TYPE_ACCOUNTTYPEID_GENERATOR", sequenceName="LKP_ACCOUNT_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_ACCOUNT_TYPE_ACCOUNTTYPEID_GENERATOR")
	@Column(name="ACCOUNT_TYPE_ID")
	private long accountTypeId;

	@Column(name="ACCOUNT_TYPE")
	private String accountType;

	@Column(name="ACCOUNT_TYPE_CODE")
	private String accountTypeCode;

	@Column(name="ACCOUNT_TYPE_DESCR")
	private String accountTypeDescr;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="SORT_SEQ")
	private BigDecimal sortSeq;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblAccount
	@OneToMany(mappedBy="lkpAccountType")
	private List<TblAccount> tblAccounts;

	//bi-directional many-to-one association to TblGlAccount
	@OneToMany(mappedBy="lkpAccountType")
	private List<TblGlAccount> tblGlAccounts;

	public LkpAccountType() {
	}

	public long getAccountTypeId() {
		return this.accountTypeId;
	}

	public void setAccountTypeId(long accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountTypeCode() {
		return this.accountTypeCode;
	}

	public void setAccountTypeCode(String accountTypeCode) {
		this.accountTypeCode = accountTypeCode;
	}

	public String getAccountTypeDescr() {
		return this.accountTypeDescr;
	}

	public void setAccountTypeDescr(String accountTypeDescr) {
		this.accountTypeDescr = accountTypeDescr;
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
		tblAccount.setLkpAccountType(this);

		return tblAccount;
	}

	public TblAccount removeTblAccount(TblAccount tblAccount) {
		getTblAccounts().remove(tblAccount);
		tblAccount.setLkpAccountType(null);

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
		tblGlAccount.setLkpAccountType(this);

		return tblGlAccount;
	}

	public TblGlAccount removeTblGlAccount(TblGlAccount tblGlAccount) {
		getTblGlAccounts().remove(tblGlAccount);
		tblGlAccount.setLkpAccountType(null);

		return tblGlAccount;
	}

}