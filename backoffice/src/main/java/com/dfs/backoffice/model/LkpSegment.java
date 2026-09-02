package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
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

	@Id
	@SequenceGenerator(name="LKP_SEGMENT_SEGMENTID_GENERATOR", sequenceName="LKP_SEGMENT_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_SEGMENT_SEGMENTID_GENERATOR")
	@Column(name="SEGMENT_ID")
	private long segmentId;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="SEGMENT_CODE")
	private String segmentCode;

	@Column(name="SEGMENT_DESCR")
	private String segmentDescr;

	private BigDecimal updateindex;

	public long getSegmentId() {
		return this.segmentId;
	}

	public void setSegmentId(long segmentId) {
		this.segmentId = segmentId;
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

}