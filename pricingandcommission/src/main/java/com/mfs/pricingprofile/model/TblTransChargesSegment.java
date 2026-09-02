package com.mfs.pricingprofile.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_TRANS_CHARGES_SEGMENT database table.
 * 
 */
@Entity
@Table(name="TBL_TRANS_CHARGES_SEGMENT")
@NamedQuery(name="TblTransChargesSegment.findAll", query="SELECT t FROM TblTransChargesSegment t")
public class TblTransChargesSegment extends TransChargesCommonEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TRANS_CHARGES_SEGMENT_TRANSCHARGESSEGMENTID_GENERATOR", sequenceName="TBL_TRANS_CHARGES_SEGMENT_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TRANS_CHARGES_SEGMENT_TRANSCHARGESSEGMENTID_GENERATOR")
	@Column(name="TRANS_CHARGES_SEGMENT_ID")
	private long transChargesSegmentId;

	@Column(name = "SEGMENT_ID")
	private BigDecimal segmentId;

	public long getTransChargesSegmentId() {
		return transChargesSegmentId;
	}

	public void setTransChargesSegmentId(long transChargesSegmentId) {
		this.transChargesSegmentId = transChargesSegmentId;
	}

	public BigDecimal getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(BigDecimal segmentId) {
		this.segmentId = segmentId;
	}
}