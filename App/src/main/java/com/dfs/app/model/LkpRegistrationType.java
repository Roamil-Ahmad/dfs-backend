package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;
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
	@SequenceGenerator(name="LKP_REGISTRATION_TYPE_REGISTRATIONTYPEID_GENERATOR", sequenceName="LKP_REGISTRATION_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_REGISTRATION_TYPE_REGISTRATIONTYPEID_GENERATOR")
	@Column(name="REGISTRATION_TYPE_ID")
	private long registrationTypeId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="REGISTRATION_TYPE_CODE")
	private String registrationTypeCode;

	@Column(name="REGISTRATION_TYPE_DESCR")
	private String registrationTypeDescr;

	@Column(name="REGISTRATION_TYPE_NAME")
	private String registrationTypeName;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblAccount
	@OneToMany(mappedBy="lkpRegistrationType")
	private List<TblAccount> tblAccounts;

	public LkpRegistrationType() {
	}

	public long getRegistrationTypeId() {
		return this.registrationTypeId;
	}

	public void setRegistrationTypeId(long registrationTypeId) {
		this.registrationTypeId = registrationTypeId;
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

	public List<TblAccount> getTblAccounts() {
		return this.tblAccounts;
	}

	public void setTblAccounts(List<TblAccount> tblAccounts) {
		this.tblAccounts = tblAccounts;
	}

	public TblAccount addTblAccount(TblAccount tblAccount) {
		getTblAccounts().add(tblAccount);
		tblAccount.setLkpRegistrationType(this);

		return tblAccount;
	}

	public TblAccount removeTblAccount(TblAccount tblAccount) {
		getTblAccounts().remove(tblAccount);
		tblAccount.setLkpRegistrationType(null);

		return tblAccount;
	}

}