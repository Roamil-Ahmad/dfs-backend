package com.mfs.pricingprofile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the LKP_STATUS database table.
 * 
 */
@Entity
@Table(name="LKP_STATUS")
@NamedQuery(name="LkpStatus.findAll", query="SELECT l FROM LkpStatus l")
public class LkpStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_STATUS_STATUSID_GENERATOR", sequenceName="LKP_STATUS_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_STATUS_STATUSID_GENERATOR")
	@Column(name="STATUS_ID")
	private long statusId;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="STATUS_CODE")
	private String statusCode;

	@Column(name="STATUS_DESCR")
	private String statusDescr;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpChannel
	@JsonIgnore
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpChannel> lkpChannels;

	//bi-directional many-to-one association to LkpSegment
	@JsonIgnore
	@OneToMany(mappedBy="lkpStatus")
	private List<LkpSegment> lkpSegments;

	//bi-directional many-to-one association to TblTransCharge
	@JsonIgnore
	@OneToMany(mappedBy="lkpStatus")
	private List<TblTransCharge> tblTransCharges;

	//bi-directional many-to-one association to TblTransDoc
	@JsonIgnore
	@OneToMany(mappedBy="lkpStatus")
	private List<TblTransDoc> tblTransDocs;

	public long getStatusId() {
		return this.statusId;
	}

	public void setStatusId(long statusId) {
		this.statusId = statusId;
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

	public String getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDescr() {
		return this.statusDescr;
	}

	public void setStatusDescr(String statusDescr) {
		this.statusDescr = statusDescr;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public List<LkpChannel> getLkpChannels() {
		return this.lkpChannels;
	}

	public void setLkpChannels(List<LkpChannel> lkpChannels) {
		this.lkpChannels = lkpChannels;
	}

	public LkpChannel addLkpChannel(LkpChannel lkpChannel) {
		getLkpChannels().add(lkpChannel);
		lkpChannel.setLkpStatus(this);

		return lkpChannel;
	}

	public LkpChannel removeLkpChannel(LkpChannel lkpChannel) {
		getLkpChannels().remove(lkpChannel);
		lkpChannel.setLkpStatus(null);

		return lkpChannel;
	}

	public List<LkpSegment> getLkpSegments() {
		return this.lkpSegments;
	}

	public void setLkpSegments(List<LkpSegment> lkpSegments) {
		this.lkpSegments = lkpSegments;
	}

	public LkpSegment addLkpSegment(LkpSegment lkpSegment) {
		getLkpSegments().add(lkpSegment);
		lkpSegment.setLkpStatus(this);

		return lkpSegment;
	}

	public LkpSegment removeLkpSegment(LkpSegment lkpSegment) {
		getLkpSegments().remove(lkpSegment);
		lkpSegment.setLkpStatus(null);

		return lkpSegment;
	}

	public List<TblTransCharge> getTblTransCharges() {
		return this.tblTransCharges;
	}

	public void setTblTransCharges(List<TblTransCharge> tblTransCharges) {
		this.tblTransCharges = tblTransCharges;
	}

	public TblTransCharge addTblTransCharge(TblTransCharge tblTransCharge) {
		getTblTransCharges().add(tblTransCharge);
		tblTransCharge.setLkpStatus(this);

		return tblTransCharge;
	}

	public TblTransCharge removeTblTransCharge(TblTransCharge tblTransCharge) {
		getTblTransCharges().remove(tblTransCharge);
		tblTransCharge.setLkpStatus(null);

		return tblTransCharge;
	}

	public List<TblTransDoc> getTblTransDocs() {
		return this.tblTransDocs;
	}

	public void setTblTransDocs(List<TblTransDoc> tblTransDocs) {
		this.tblTransDocs = tblTransDocs;
	}

	public TblTransDoc addTblTransDoc(TblTransDoc tblTransDoc) {
		getTblTransDocs().add(tblTransDoc);
		tblTransDoc.setLkpStatus(this);

		return tblTransDoc;
	}

	public TblTransDoc removeTblTransDoc(TblTransDoc tblTransDoc) {
		getTblTransDocs().remove(tblTransDoc);
		tblTransDoc.setLkpStatus(null);

		return tblTransDoc;
	}

}