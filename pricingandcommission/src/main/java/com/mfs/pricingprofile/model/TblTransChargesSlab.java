package com.mfs.pricingprofile.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_TRANS_CHARGES_SLABS database table.
 * 
 */
@Entity
@Table(name="TBL_TRANS_CHARGES_SLABS")
@NamedQuery(name="TblTransChargesSlab.findAll", query="SELECT t FROM TblTransChargesSlab t")
public class TblTransChargesSlab extends TransChargesCommonEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TRANS_CHARGES_SLABS_TRANSCHARGESSLABSID_GENERATOR", sequenceName="TBL_TRANS_CHARGES_SLABS_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TRANS_CHARGES_SLABS_TRANSCHARGESSLABSID_GENERATOR")
	@Column(name="TRANS_CHARGES_SLABS_ID")
	private long transChargesSlabsId;

	private BigDecimal amount;

	@Column(name="FEE_CONDITION")
	private String feeCondition;

	private BigDecimal fromamount;

	@Column(name="ONELINK_SHARE")
	private BigDecimal onelinkShare;

	@Column(name="ONELINK_SHARE_TYPE")
	private String onelinkShareType;

	private String oprtr;

	private BigDecimal percentage;

	private BigDecimal toamount;

	public long getTransChargesSlabsId() {
		return transChargesSlabsId;
	}

	public void setTransChargesSlabsId(long transChargesSlabsId) {
		this.transChargesSlabsId = transChargesSlabsId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getFeeCondition() {
		return feeCondition;
	}

	public void setFeeCondition(String feeCondition) {
		this.feeCondition = feeCondition;
	}

	public BigDecimal getFromamount() {
		return fromamount;
	}

	public void setFromamount(BigDecimal fromamount) {
		this.fromamount = fromamount;
	}

	public BigDecimal getOnelinkShare() {
		return onelinkShare;
	}

	public void setOnelinkShare(BigDecimal onelinkShare) {
		this.onelinkShare = onelinkShare;
	}

	public String getOnelinkShareType() {
		return onelinkShareType;
	}

	public void setOnelinkShareType(String onelinkShareType) {
		this.onelinkShareType = onelinkShareType;
	}

	public String getOprtr() {
		return oprtr;
	}

	public void setOprtr(String oprtr) {
		this.oprtr = oprtr;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public BigDecimal getToamount() {
		return toamount;
	}

	public void setToamount(BigDecimal toamount) {
		this.toamount = toamount;
	}
}