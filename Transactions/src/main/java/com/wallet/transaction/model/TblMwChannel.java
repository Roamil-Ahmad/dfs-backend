package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_MW_CHANNEL database table.
 * 
 */
@Entity
@Table(name="TBL_MW_CHANNEL")
@NamedQuery(name="TblMwChannel.findAll", query="SELECT t FROM TblMwChannel t")
public class TblMwChannel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_MW_CHANNEL_MWCHANNELID_GENERATOR", sequenceName="TBL_MW_CHANNEL_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MW_CHANNEL_MWCHANNELID_GENERATOR")
	@Column(name="MW_CHANNEL_ID")
	private long mwChannelId;

	@Column(name="CHANNEL_CODE")
	private String channelCode;

	@Column(name="CHANNEL_DESCR")
	private String channelDescr;

	@Column(name="CLIENT_SECRET")
	private String clientSecret;

	private Date createdate;

	private BigDecimal createuser;

	private String dflt;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	@Column(name="USER_ID")
	private BigDecimal userId;

	public TblMwChannel() {
	}

	public long getMwChannelId() {
		return this.mwChannelId;
	}

	public void setMwChannelId(long mwChannelId) {
		this.mwChannelId = mwChannelId;
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

	public String getDflt() {
		return this.dflt;
	}

	public void setDflt(String dflt) {
		this.dflt = dflt;
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

	public BigDecimal getUserId() {
		return this.userId;
	}

	public void setUserId(BigDecimal userId) {
		this.userId = userId;
	}

}