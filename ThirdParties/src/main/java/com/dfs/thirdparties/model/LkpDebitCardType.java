package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the LKP_DEBIT_CARD_TYPE database table.
 * 
 */
@Entity
@Table(name="LKP_DEBIT_CARD_TYPE")
@NamedQuery(name="LkpDebitCardType.findAll", query="SELECT l FROM LkpDebitCardType l")
public class LkpDebitCardType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_DEBIT_CARD_TYPE_DEBITCARDTYPEID_GENERATOR", sequenceName="LKP_DEBIT_CARD_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_DEBIT_CARD_TYPE_DEBITCARDTYPEID_GENERATOR")
	@Column(name="DEBIT_CARD_TYPE_ID")
	private long debitCardTypeId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DEBIT_CARD_TYPE_CODE")
	private String debitCardTypeCode;

	@Column(name="DEBIT_CARD_TYPE_DESCR")
	private String debitCardTypeDescr;

	@Column(name="DELIVERY_CHARGES")
	private BigDecimal deliveryCharges;

	private BigDecimal fee;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblDebitCardRequest
	@OneToMany(mappedBy="lkpDebitCardType")
	private List<TblDebitCardRequest> tblDebitCardRequests;

	public LkpDebitCardType() {
	}

	public long getDebitCardTypeId() {
		return this.debitCardTypeId;
	}

	public void setDebitCardTypeId(long debitCardTypeId) {
		this.debitCardTypeId = debitCardTypeId;
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

	public String getDebitCardTypeCode() {
		return this.debitCardTypeCode;
	}

	public void setDebitCardTypeCode(String debitCardTypeCode) {
		this.debitCardTypeCode = debitCardTypeCode;
	}

	public String getDebitCardTypeDescr() {
		return this.debitCardTypeDescr;
	}

	public void setDebitCardTypeDescr(String debitCardTypeDescr) {
		this.debitCardTypeDescr = debitCardTypeDescr;
	}

	public BigDecimal getDeliveryCharges() {
		return this.deliveryCharges;
	}

	public void setDeliveryCharges(BigDecimal deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	public BigDecimal getFee() {
		return this.fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
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

	public List<TblDebitCardRequest> getTblDebitCardRequests() {
		return this.tblDebitCardRequests;
	}

	public void setTblDebitCardRequests(List<TblDebitCardRequest> tblDebitCardRequests) {
		this.tblDebitCardRequests = tblDebitCardRequests;
	}

	public TblDebitCardRequest addTblDebitCardRequest(TblDebitCardRequest tblDebitCardRequest) {
		getTblDebitCardRequests().add(tblDebitCardRequest);
		tblDebitCardRequest.setLkpDebitCardType(this);

		return tblDebitCardRequest;
	}

	public TblDebitCardRequest removeTblDebitCardRequest(TblDebitCardRequest tblDebitCardRequest) {
		getTblDebitCardRequests().remove(tblDebitCardRequest);
		tblDebitCardRequest.setLkpDebitCardType(null);

		return tblDebitCardRequest;
	}

}