package com.dfs.backoffice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_GL_ACCOUNT database table.
 * 
 */
@Entity
@Table(name="TBL_GL_ACCOUNT")
@NamedQuery(name="TblGlAccount.findAll", query="SELECT t FROM TblGlAccount t")
public class TblGlAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_GL_ACCOUNT_GLACCOUNTID_GENERATOR", sequenceName="TBL_GL_ACCOUNT_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_GL_ACCOUNT_GLACCOUNTID_GENERATOR")
	@Column(name="GL_ACCOUNT_ID")
	private long glAccountId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="CURRENT_BALANCE")
	private BigDecimal currentBalance;

	private BigDecimal depth;

	@Column(name="GL_ACCOUNT_CODE")
	private String glAccountCode;

	@Column(name="GL_ACCOUNT_DESCR")
	private String glAccountDescr;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal overdrawn;

	@Column(name="PAR_REF_ID")
	private BigDecimal parRefId;

	@Column(name="STATUS_ID")
	private BigDecimal statusId;

	private BigDecimal updateindex;

	@Column(name="ACCOUNT_STATUS_ID")
	private BigDecimal accountStatusId;

	@Column(name="ACCOUNT_TYPE_ID")
	private BigDecimal accountTypeId;


	public long getGlAccountId() {
		return this.glAccountId;
	}

	public void setGlAccountId(long glAccountId) {
		this.glAccountId = glAccountId;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getLastupdatedate() {
		return lastupdatedate;
	}

	public void setLastupdatedate(Date lastupdatedate) {
		this.lastupdatedate = lastupdatedate;
	}

	public BigDecimal getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(BigDecimal createuser) {
		this.createuser = createuser;
	}

	public BigDecimal getCurrentBalance() {
		return this.currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public BigDecimal getDepth() {
		return this.depth;
	}

	public void setDepth(BigDecimal depth) {
		this.depth = depth;
	}

	public String getGlAccountCode() {
		return this.glAccountCode;
	}

	public void setGlAccountCode(String glAccountCode) {
		this.glAccountCode = glAccountCode;
	}

	public String getGlAccountDescr() {
		return this.glAccountDescr;
	}

	public void setGlAccountDescr(String glAccountDescr) {
		this.glAccountDescr = glAccountDescr;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getLastupdateuser() {
		return this.lastupdateuser;
	}

	public void setLastupdateuser(BigDecimal lastupdateuser) {
		this.lastupdateuser = lastupdateuser;
	}

	public BigDecimal getOverdrawn() {
		return this.overdrawn;
	}

	public void setOverdrawn(BigDecimal overdrawn) {
		this.overdrawn = overdrawn;
	}

	public BigDecimal getParRefId() {
		return this.parRefId;
	}

	public void setParRefId(BigDecimal parRefId) {
		this.parRefId = parRefId;
	}

	public BigDecimal getStatusId() {
		return this.statusId;
	}

	public void setStatusId(BigDecimal statusId) {
		this.statusId = statusId;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public BigDecimal getAccountStatusId() {
		return accountStatusId;
	}

	public void setAccountStatusId(BigDecimal accountStatusId) {
		this.accountStatusId = accountStatusId;
	}

	public BigDecimal getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(BigDecimal accountTypeId) {
		this.accountTypeId = accountTypeId;
	}
}