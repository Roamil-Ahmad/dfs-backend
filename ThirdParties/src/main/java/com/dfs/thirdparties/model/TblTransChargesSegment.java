package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_TRANS_CHARGES_SEGMENT database table.
 * 
 */
@Entity
@Table(name="TBL_TRANS_CHARGES_SEGMENT")
@NamedQuery(name="TblTransChargesSegment.findAll", query="SELECT t FROM TblTransChargesSegment t")
public class TblTransChargesSegment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TRANS_CHARGES_SEGMENT_TRANSCHARGESSEGMENTID_GENERATOR", sequenceName="TBL_TRANS_CHARGES_SEGMENT_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TRANS_CHARGES_SEGMENT_TRANSCHARGESSEGMENTID_GENERATOR")
	@Column(name="TRANS_CHARGES_SEGMENT_ID")
	private long transChargesSegmentId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpSegment
	@ManyToOne
	@JoinColumn(name="SEGMENT_ID")
	private LkpSegment lkpSegment;

	//bi-directional many-to-one association to TblTransCharge
	@ManyToOne
	@JoinColumn(name="TRANS_CHARGES_ID")
	private TblTransCharge tblTransCharge;

	public TblTransChargesSegment() {
	}

	public long getTransChargesSegmentId() {
		return this.transChargesSegmentId;
	}

	public void setTransChargesSegmentId(long transChargesSegmentId) {
		this.transChargesSegmentId = transChargesSegmentId;
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

	public LkpSegment getLkpSegment() {
		return this.lkpSegment;
	}

	public void setLkpSegment(LkpSegment lkpSegment) {
		this.lkpSegment = lkpSegment;
	}

	public TblTransCharge getTblTransCharge() {
		return this.tblTransCharge;
	}

	public void setTblTransCharge(TblTransCharge tblTransCharge) {
		this.tblTransCharge = tblTransCharge;
	}

}