package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the LKP_REGISTRATION_TYPE database table.
 * 
 */
@Entity
@Table(name="LKP_REGISTRATION_TYPE")
@NamedQuery(name="LkpRegistrationType.findAll", query="SELECT l FROM LkpRegistrationType l")
public class LkpRegistrationType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_REGISTRATION_TYPE_REGISTRATIONTYPEID_GENERATOR", sequenceName="LKP_REGISTRATION_TYPE_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_REGISTRATION_TYPE_REGISTRATIONTYPEID_GENERATOR")
	@Column(name="REGISTRATION_TYPE_ID")
	private long registrationTypeId;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="REGISTRATION_TYPE_CODE")
	private String registrationTypeCode;

	@Column(name="REGISTRATION_TYPE_DESCR")
	private String registrationTypeDescr;

	@Column(name="REGISTRATION_TYPE_NAME")
	private String registrationTypeName;

	private BigDecimal updateindex;

	public long getRegistrationTypeId() {
		return this.registrationTypeId;
	}

	public void setRegistrationTypeId(long registrationTypeId) {
		this.registrationTypeId = registrationTypeId;
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

	public String getRegistrationTypeCode() {
		return this.registrationTypeCode;
	}

	public void setRegistrationTypeCode(String registrationTypeCode) {
		this.registrationTypeCode = registrationTypeCode;
	}

	public String getRegistrationTypeDescr() {
		return this.registrationTypeDescr;
	}

	public void setRegistrationTypeDescr(String registrationTypeDescr) {
		this.registrationTypeDescr = registrationTypeDescr;
	}

	public String getRegistrationTypeName() {
		return this.registrationTypeName;
	}

	public void setRegistrationTypeName(String registrationTypeName) {
		this.registrationTypeName = registrationTypeName;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}