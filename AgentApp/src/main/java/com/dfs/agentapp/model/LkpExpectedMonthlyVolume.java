package com.dfs.agentapp.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the LKP_EXPECTED_MONTHLY_VOLUME database table.
 */
@Entity
@Table(name="LKP_EXPECTED_MONTHLY_VOLUME")
@NamedQuery(name="LkpExpectedMonthlyVolume.findAll", query="SELECT l FROM LkpExpectedMonthlyVolume l")
public class LkpExpectedMonthlyVolume implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EXPECTED_MONTHLY_VOLUME_ID")
	private long expectedMonthlyVolumeId;

	@Column(name="EXPECTED_MONTHLY_VOLUME_CODE")
	private String expectedMonthlyVolumeCode;

	@Column(name="EXPECTED_MONTHLY_VOLUME_DESCR")
	private String expectedMonthlyVolumeDescr;

	@Column(name="EXPECTED_MONTHLY_VOLUME_NAME")
	private String expectedMonthlyVolumeName;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	public LkpExpectedMonthlyVolume() {
	}

	public long getExpectedMonthlyVolumeId() { return this.expectedMonthlyVolumeId; }
	public void setExpectedMonthlyVolumeId(long expectedMonthlyVolumeId) { this.expectedMonthlyVolumeId = expectedMonthlyVolumeId; }

	public String getExpectedMonthlyVolumeCode() { return this.expectedMonthlyVolumeCode; }
	public void setExpectedMonthlyVolumeCode(String expectedMonthlyVolumeCode) { this.expectedMonthlyVolumeCode = expectedMonthlyVolumeCode; }

	public String getExpectedMonthlyVolumeDescr() { return this.expectedMonthlyVolumeDescr; }
	public void setExpectedMonthlyVolumeDescr(String expectedMonthlyVolumeDescr) { this.expectedMonthlyVolumeDescr = expectedMonthlyVolumeDescr; }

	public String getExpectedMonthlyVolumeName() { return this.expectedMonthlyVolumeName; }
	public void setExpectedMonthlyVolumeName(String expectedMonthlyVolumeName) { this.expectedMonthlyVolumeName = expectedMonthlyVolumeName; }

	public String getIsActive() { return this.isActive; }
	public void setIsActive(String isActive) { this.isActive = isActive; }

	public Date getCreatedate() { return this.createdate; }
	public void setCreatedate(Date createdate) { this.createdate = createdate; }

	public BigDecimal getCreateuser() { return this.createuser; }
	public void setCreateuser(BigDecimal createuser) { this.createuser = createuser; }

	public Date getLastupdatedate() { return this.lastupdatedate; }
	public void setLastupdatedate(Date lastupdatedate) { this.lastupdatedate = lastupdatedate; }

	public BigDecimal getLastupdateuser() { return this.lastupdateuser; }
	public void setLastupdateuser(BigDecimal lastupdateuser) { this.lastupdateuser = lastupdateuser; }

	public BigDecimal getUpdateindex() { return this.updateindex; }
	public void setUpdateindex(BigDecimal updateindex) { this.updateindex = updateindex; }
}
