package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_TRANS_CHARGES_SLABS database table.
 * 
 */
@Entity
@Table(name="TBL_TRANS_CHARGES_SLABS")
@NamedQuery(name="TblTransChargesSlab.findAll", query="SELECT t FROM TblTransChargesSlab t")
public class TblTransChargesSlab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TRANS_CHARGES_SLABS_TRANSCHARGESSLABSID_GENERATOR", sequenceName="TBL_TRANS_CHARGES_SLABS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TRANS_CHARGES_SLABS_TRANSCHARGESSLABSID_GENERATOR")
	@Column(name="TRANS_CHARGES_SLABS_ID")
	private long transChargesSlabsId;

	private BigDecimal amount;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="FEE_CONDITION")
	private String feeCondition;

	private BigDecimal fromamount;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="ONELINK_SHARE")
	private BigDecimal onelinkShare;

	@Column(name="ONELINK_SHARE_TYPE")
	private String onelinkShareType;

	private String oprtr;

	private BigDecimal percentage;

	private BigDecimal toamount;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblTransCharge
	@ManyToOne
	@JoinColumn(name="TRANS_CHARGES_ID")
	private TblTransCharge tblTransCharge;

	public TblTransChargesSlab() {
	}

	public long getTransChargesSlabsId() {
		return this.transChargesSlabsId;
	}

	public void setTransChargesSlabsId(long transChargesSlabsId) {
		this.transChargesSlabsId = transChargesSlabsId;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public String getFeeCondition() {
		return this.feeCondition;
	}

	public void setFeeCondition(String feeCondition) {
		this.feeCondition = feeCondition;
	}

	public BigDecimal getFromamount() {
		return this.fromamount;
	}

	public void setFromamount(BigDecimal fromamount) {
		this.fromamount = fromamount;
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

	public BigDecimal getOnelinkShare() {
		return this.onelinkShare;
	}

	public void setOnelinkShare(BigDecimal onelinkShare) {
		this.onelinkShare = onelinkShare;
	}

	public String getOnelinkShareType() {
		return this.onelinkShareType;
	}

	public void setOnelinkShareType(String onelinkShareType) {
		this.onelinkShareType = onelinkShareType;
	}

	public String getOprtr() {
		return this.oprtr;
	}

	public void setOprtr(String oprtr) {
		this.oprtr = oprtr;
	}

	public BigDecimal getPercentage() {
		return this.percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public BigDecimal getToamount() {
		return this.toamount;
	}

	public void setToamount(BigDecimal toamount) {
		this.toamount = toamount;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public TblTransCharge getTblTransCharge() {
		return this.tblTransCharge;
	}

	public void setTblTransCharge(TblTransCharge tblTransCharge) {
		this.tblTransCharge = tblTransCharge;
	}

}