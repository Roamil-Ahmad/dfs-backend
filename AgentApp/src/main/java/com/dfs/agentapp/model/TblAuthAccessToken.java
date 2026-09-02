package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_AUTH_ACCESS_TOKEN database table.
 * 
 */
@Entity
@Table(name="TBL_AUTH_ACCESS_TOKEN")
@NamedQuery(name="TblAuthAccessToken.findAll", query="SELECT t FROM TblAuthAccessToken t")
public class TblAuthAccessToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_AUTH_ACCESS_TOKEN_AUTHACCESSTOKENID_GENERATOR", sequenceName="TBL_AUTH_ACCESS_TOKEN_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_AUTH_ACCESS_TOKEN_AUTHACCESSTOKENID_GENERATOR")
	@Column(name="AUTH_ACCESS_TOKEN_ID")
	private long authAccessTokenId;

	@Column(name="ACCESS_TOKEN")
	private String accessToken;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="EFFECTIVE_FROM")
	private Date effectiveFrom;

	@Column(name="EFFECTIVE_TO")
	private Date effectiveTo;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpChannel
	@ManyToOne
	@JoinColumn(name="CHANNEL_ID")
	private LkpChannel lkpChannel;

	//bi-directional many-to-one association to TblAppUserLoginHistory
	@ManyToOne
	@JoinColumn(name="APP_USER_LOGIN_HISTORY_ID")
	private TblAppUserLoginHistory tblAppUserLoginHistory;

	//bi-directional many-to-one association to TblAppUserLoginHistory
	@ManyToOne
	@JoinColumn(name="CUSTOMER_ALL_ID")
	private TblCustomerAll tblCustomerAll;

	public TblAuthAccessToken() {
	}

	public long getAuthAccessTokenId() {
		return this.authAccessTokenId;
	}

	public void setAuthAccessTokenId(long authAccessTokenId) {
		this.authAccessTokenId = authAccessTokenId;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
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

	public Date getEffectiveFrom() {
		return this.effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Date getEffectiveTo() {
		return this.effectiveTo;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
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

	public LkpChannel getLkpChannel() {
		return this.lkpChannel;
	}

	public void setLkpChannel(LkpChannel lkpChannel) {
		this.lkpChannel = lkpChannel;
	}

	public TblAppUserLoginHistory getTblAppUserLoginHistory() {
		return this.tblAppUserLoginHistory;
	}

	public void setTblAppUserLoginHistory(TblAppUserLoginHistory tblAppUserLoginHistory) {
		this.tblAppUserLoginHistory = tblAppUserLoginHistory;
	}

	public TblCustomerAll getTblCustomerAll() {
		return tblCustomerAll;
	}

	public void setTblCustomerAll(TblCustomerAll tblCustomerAll) {
		this.tblCustomerAll = tblCustomerAll;
	}
}