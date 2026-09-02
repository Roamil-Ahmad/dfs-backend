package com.mfs.pricingprofile.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_TRANS_CHARGES_CHANNEL database table.
 * 
 */
@Entity
@Table(name="TBL_TRANS_CHARGES_CHANNEL")
@NamedQuery(name="TblTransChargesChannel.findAll", query="SELECT t FROM TblTransChargesChannel t")
public class TblTransChargesChannel extends TransChargesCommonEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TRANS_CHARGES_CHANNEL_TRANSCHARGESCHANNELID_GENERATOR", sequenceName="TBL_TRANS_CHARGES_CHANNEL_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TRANS_CHARGES_CHANNEL_TRANSCHARGESCHANNELID_GENERATOR")
	@Column(name="TRANS_CHARGES_CHANNEL_ID")
	private long transChargesChannelId;

	@Column(name = "CHANNEL_ID")
	private BigDecimal channelId;

	public long getTransChargesChannelId() {
		return transChargesChannelId;
	}

	public void setTransChargesChannelId(long transChargesChannelId) {
		this.transChargesChannelId = transChargesChannelId;
	}

	public BigDecimal getChannelId() {
		return channelId;
	}

	public void setChannelId(BigDecimal channelId) {
		this.channelId = channelId;
	}
}