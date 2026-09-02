package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
	@SequenceGenerator(name="LKP_CHANNEL_CHANNELID_GENERATOR", sequenceName="LKP_CHANNEL_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_CHANNEL_CHANNELID_GENERATOR")
	@Column(name="CHANNEL_ID")
	private long channelId;

	@Column(name="CHANNEL_CODE")
	private String channelCode;

	@Column(name="CHANNEL_DESCR")
	private String channelDescr;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblCustomerAll
	@OneToMany(mappedBy="lkpChannel")
	private List<TblCustomerAll> tblCustomerAlls;

	//bi-directional many-to-one association to TblTransChargesChannel
	@OneToMany(mappedBy="lkpChannel")
	private List<TblTransChargesChannel> tblTransChargesChannels;

	//bi-directional many-to-one association to TblTransHead
	@OneToMany(mappedBy="lkpChannel")
	private List<TblTransHead> tblTransHeads;

	public LkpChannel() {
	}

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

	public List<TblCustomerAll> getTblCustomerAlls() {
		return this.tblCustomerAlls;
	}

	public void setTblCustomerAlls(List<TblCustomerAll> tblCustomerAlls) {
		this.tblCustomerAlls = tblCustomerAlls;
	}

	public TblCustomerAll addTblCustomerAll(TblCustomerAll tblCustomerAll) {
		getTblCustomerAlls().add(tblCustomerAll);
		tblCustomerAll.setLkpChannel(this);

		return tblCustomerAll;
	}

	public TblCustomerAll removeTblCustomerAll(TblCustomerAll tblCustomerAll) {
		getTblCustomerAlls().remove(tblCustomerAll);
		tblCustomerAll.setLkpChannel(null);

		return tblCustomerAll;
	}

	public List<TblTransChargesChannel> getTblTransChargesChannels() {
		return this.tblTransChargesChannels;
	}

	public void setTblTransChargesChannels(List<TblTransChargesChannel> tblTransChargesChannels) {
		this.tblTransChargesChannels = tblTransChargesChannels;
	}

	public TblTransChargesChannel addTblTransChargesChannel(TblTransChargesChannel tblTransChargesChannel) {
		getTblTransChargesChannels().add(tblTransChargesChannel);
		tblTransChargesChannel.setLkpChannel(this);

		return tblTransChargesChannel;
	}

	public TblTransChargesChannel removeTblTransChargesChannel(TblTransChargesChannel tblTransChargesChannel) {
		getTblTransChargesChannels().remove(tblTransChargesChannel);
		tblTransChargesChannel.setLkpChannel(null);

		return tblTransChargesChannel;
	}

	public List<TblTransHead> getTblTransHeads() {
		return this.tblTransHeads;
	}

	public void setTblTransHeads(List<TblTransHead> tblTransHeads) {
		this.tblTransHeads = tblTransHeads;
	}

	public TblTransHead addTblTransHead(TblTransHead tblTransHead) {
		getTblTransHeads().add(tblTransHead);
		tblTransHead.setLkpChannel(this);

		return tblTransHead;
	}

	public TblTransHead removeTblTransHead(TblTransHead tblTransHead) {
		getTblTransHeads().remove(tblTransHead);
		tblTransHead.setLkpChannel(null);

		return tblTransHead;
	}

}