package com.dfs.agentapp.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the LKP_LICENSE_TYPE database table.
 * 
 */
@Entity
@Table(name="LKP_LICENSE_TYPE")
@NamedQuery(name="LkpLicenseType.findAll", query="SELECT l FROM LkpLicenseType l")
public class LkpLicenseType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_LICENSE_TYPE_LICENSETYPEID_GENERATOR", sequenceName="LKP_LICENSE_TYPE_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_LICENSE_TYPE_LICENSETYPEID_GENERATOR")
	@Column(name="LICENSE_TYPE_ID")
	private long licenseTypeId;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="LICENSE_TYPE_CODE")
	private String licenseTypeCode;

	@Column(name="LICENSE_TYPE_DESCR")
	private String licenseTypeDescr;

	@Column(name="LICENSE_TYPE_NAME")
	private String licenseTypeName;

	private BigDecimal updateindex;

	public LkpLicenseType() {
	}

	public long getLicenseTypeId() {
		return this.licenseTypeId;
	}

	public void setLicenseTypeId(long licenseTypeId) {
		this.licenseTypeId = licenseTypeId;
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

	public String getLicenseTypeCode() {
		return this.licenseTypeCode;
	}

	public void setLicenseTypeCode(String licenseTypeCode) {
		this.licenseTypeCode = licenseTypeCode;
	}

	public String getLicenseTypeDescr() {
		return this.licenseTypeDescr;
	}

	public void setLicenseTypeDescr(String licenseTypeDescr) {
		this.licenseTypeDescr = licenseTypeDescr;
	}

	public String getLicenseTypeName() {
		return this.licenseTypeName;
	}

	public void setLicenseTypeName(String licenseTypeName) {
		this.licenseTypeName = licenseTypeName;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}