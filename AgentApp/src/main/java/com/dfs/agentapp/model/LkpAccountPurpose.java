package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the LKP_ACCOUNT_PURPOSE database table.
 * 
 */
@Entity
@Table(name="LKP_ACCOUNT_PURPOSE")
@NamedQuery(name="LkpAccountPurpose.findAll", query="SELECT l FROM LkpAccountPurpose l")
public class LkpAccountPurpose implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_ACCOUNT_PURPOSE_ACCOUNTPURPOSEID_GENERATOR", sequenceName="LKP_ACCOUNT_PURPOSE_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_ACCOUNT_PURPOSE_ACCOUNTPURPOSEID_GENERATOR")
	@Column(name="ACCOUNT_PURPOSE_ID")
	private long accountPurposeId;

	@Column(name="ACCOUNT_PURPOSE_CODE")
	private String accountPurposeCode;

	@Column(name="ACCOUNT_PURPOSE_DESCR")
	private String accountPurposeDescr;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	private BigDecimal updateindex;

	public LkpAccountPurpose() {
	}

	public long getAccountPurposeId() {
		return this.accountPurposeId;
	}

	public void setAccountPurposeId(long accountPurposeId) {
		this.accountPurposeId = accountPurposeId;
	}

	public String getAccountPurposeCode() {
		return this.accountPurposeCode;
	}

	public void setAccountPurposeCode(String accountPurposeCode) {
		this.accountPurposeCode = accountPurposeCode;
	}

	public String getAccountPurposeDescr() {
		return this.accountPurposeDescr;
	}

	public void setAccountPurposeDescr(String accountPurposeDescr) {
		this.accountPurposeDescr = accountPurposeDescr;
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


	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public LkpStatus getLkpStatus() {
		return lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}
}