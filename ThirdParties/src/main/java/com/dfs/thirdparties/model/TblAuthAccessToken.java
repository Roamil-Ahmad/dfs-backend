package com.dfs.thirdparties.model;

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

	@Column(name="CUSTOMER_ALL_ID")
	private BigDecimal customerAllId;

	@Column(name="EFFECTIVE_FROM")
	private Date effectiveFrom;

	@Column(name="EFFECTIVE_TO")
	private Date effectiveTo;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

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

	public BigDecimal getCustomerAllId() {
		return this.customerAllId;
	}

	public void setCustomerAllId(BigDecimal customerAllId) {
		this.customerAllId = customerAllId;
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

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}