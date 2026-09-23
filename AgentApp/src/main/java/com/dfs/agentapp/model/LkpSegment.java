package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the LKP_SEGMENT database table.
 * 
 */
@Entity
@Table(name="LKP_SEGMENT")
@NamedQuery(name="LkpSegment.findAll", query="SELECT l FROM LkpSegment l")
public class LkpSegment implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Assigned by the application, not by the database.
	 *
	 * <p>This was mapped to the sequence LKP_SEGMENT_SEQ, which does not exist in this schema, so
	 * every insert failed with ORA-02289. Creating the sequence would be a schema change, so the
	 * caller sets the id before saving instead.</p>
	 */
	@Id
	@Column(name="SEGMENT_ID")
	private long segmentId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="SEGMENT_CODE")
	private String segmentCode;

	@Column(name="SEGMENT_DESCR")
	private String segmentDescr;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblTransChargesSegment
	@OneToMany(mappedBy="lkpSegment")
	private List<TblTransChargesSegment> tblTransChargesSegments;

	public LkpSegment() {
	}

	public long getSegmentId() {
		return this.segmentId;
	}

	public void setSegmentId(long segmentId) {
		this.segmentId = segmentId;
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

	public String getSegmentCode() {
		return this.segmentCode;
	}

	public void setSegmentCode(String segmentCode) {
		this.segmentCode = segmentCode;
	}

	public String getSegmentDescr() {
		return this.segmentDescr;
	}

	public void setSegmentDescr(String segmentDescr) {
		this.segmentDescr = segmentDescr;
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

	public List<TblTransChargesSegment> getTblTransChargesSegments() {
		return this.tblTransChargesSegments;
	}

	public void setTblTransChargesSegments(List<TblTransChargesSegment> tblTransChargesSegments) {
		this.tblTransChargesSegments = tblTransChargesSegments;
	}

	public TblTransChargesSegment addTblTransChargesSegment(TblTransChargesSegment tblTransChargesSegment) {
		getTblTransChargesSegments().add(tblTransChargesSegment);
		tblTransChargesSegment.setLkpSegment(this);

		return tblTransChargesSegment;
	}

	public TblTransChargesSegment removeTblTransChargesSegment(TblTransChargesSegment tblTransChargesSegment) {
		getTblTransChargesSegments().remove(tblTransChargesSegment);
		tblTransChargesSegment.setLkpSegment(null);

		return tblTransChargesSegment;
	}

}