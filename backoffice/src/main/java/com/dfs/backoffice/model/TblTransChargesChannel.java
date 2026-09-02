package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_TRANS_CHARGES_CHANNEL database table.
 * 
 */
@Entity
@Table(name="TBL_TRANS_CHARGES_CHANNEL")
@NamedQuery(name="TblTransChargesChannel.findAll", query="SELECT t FROM TblTransChargesChannel t")
public class TblTransChargesChannel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TRANS_CHARGES_CHANNEL_TRANSCHARGESCHANNELID_GENERATOR", sequenceName="TBL_TRANS_CHARGES_CHANNEL_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TRANS_CHARGES_CHANNEL_TRANSCHARGESCHANNELID_GENERATOR")
	@Column(name="TRANS_CHARGES_CHANNEL_ID")
	private long transChargesChannelId;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpChannel
	@ManyToOne
	@JoinColumn(name="CHANNEL_ID")
	private LkpChannel lkpChannel;

	//bi-directional many-to-one association to TblTransCharge
	@ManyToOne
	@JoinColumn(name="TRANS_CHARGES_ID")
	private TblTransCharge tblTransCharge;

	public TblTransChargesChannel() {
	}

	public long getTransChargesChannelId() {
		return this.transChargesChannelId;
	}

	public void setTransChargesChannelId(long transChargesChannelId) {
		this.transChargesChannelId = transChargesChannelId;
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

	public LkpChannel getLkpChannel() {
		return this.lkpChannel;
	}

	public void setLkpChannel(LkpChannel lkpChannel) {
		this.lkpChannel = lkpChannel;
	}

	public TblTransCharge getTblTransCharge() {
		return this.tblTransCharge;
	}

	public void setTblTransCharge(TblTransCharge tblTransCharge) {
		this.tblTransCharge = tblTransCharge;
	}

}