package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the LKP_CHANNEL database table.
 * 
 */
@Entity
@Table(name="LKP_CHANNEL")
@NamedQuery(name="LkpChannel.findAll", query="SELECT l FROM LkpChannel l")
public class LkpChannel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_CHANNEL_CHANNELID_GENERATOR", sequenceName="LKP_CHANNEL_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_CHANNEL_CHANNELID_GENERATOR")
	@Column(name="CHANNEL_ID")
	private long channelId;

	@Column(name="CHANNEL_CODE")
	private String channelCode;

	@Column(name="CHANNEL_DESCR")
	private String channelDescr;

	@Column(name="CLIENT_SECRET")
	private String clientSecret;

	private Timestamp createdate;

	private BigDecimal createuser;

	private String dflt;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	public long getChannelId() {
		return this.channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public String getChannelCode() {
		return this.channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelDescr() {
		return this.channelDescr;
	}

	public void setChannelDescr(String channelDescr) {
		this.channelDescr = channelDescr;
	}

	public String getClientSecret() {
		return this.clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
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

	public String getDflt() {
		return this.dflt;
	}

	public void setDflt(String dflt) {
		this.dflt = dflt;
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

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}
}