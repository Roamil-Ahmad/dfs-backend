package com.barq.nadra.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_NID_MOCK database table.
 *
 * Reference data used by the mock verification flows (cnicVerification / bioVerification):
 * one row per NID number holding the citizen record that is returned instead of calling the
 * live NADRA service. The service only ever READS this table.
 */
@Entity
@Table(name="TBL_NID_MOCK")
@NamedQuery(name="TblNidMock.findAll", query="SELECT t FROM TblNidMock t")
public class TblNidMock implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="NID_NO")
	private String nidNo;

	private String name;

	@Column(name="NAME_EN")
	private String nameEn;

	@Column(name="FATHER_HUSBAND_NAME_EN")
	private String fatherHusbandNameEn;

	@Column(name="MOTHER_NAME")
	private String motherName;

	@Column(name="MOTHER_NAME_EN")
	private String motherNameEn;

	@Column(name="PRESENT_ADDRESS")
	private String presentAddress;

	@Column(name="PRESENT_ADDRESS_EN")
	private String presentAddressEn;

	@Column(name="PERMANENT_ADDRESS_EN")
	private String permanentAddressEn;

	/** NOTE: the physical column really is spelled BIRTH_PALCE in TBL_NID_MOCK. */
	@Column(name="BIRTH_PALCE")
	private String birthPlace;

	@Column(name="BIRTH_PLACE_EN")
	private String birthPlaceEn;

	@Column(name="DATE_OF_BIRTH")
	private String dateOfBirth;

	private String gender;

	@Temporal(TemporalType.DATE)
	@Column(name="ISSUANCE_DATE")
	private Date issuanceDate;

	@Column(name="EXPIRY_DATE")
	private String expiryDate;

	@Column(name="STATUS_CODE")
	private String statusCode;

	@Column(name="STATUS_MESSAGE")
	private String statusMessage;

	@Column(name="TRANSLATION_ID")
	private String translationId;

	@Column(name="SESSION_ID")
	private String sessionId;

	private BigDecimal id;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	public TblNidMock() {
	}

	public String getNidNo() {
		return this.nidNo;
	}

	public void setNidNo(String nidNo) {
		this.nidNo = nidNo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getFatherHusbandNameEn() {
		return this.fatherHusbandNameEn;
	}

	public void setFatherHusbandNameEn(String fatherHusbandNameEn) {
		this.fatherHusbandNameEn = fatherHusbandNameEn;
	}

	public String getMotherName() {
		return this.motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getMotherNameEn() {
		return this.motherNameEn;
	}

	public void setMotherNameEn(String motherNameEn) {
		this.motherNameEn = motherNameEn;
	}

	public String getPresentAddress() {
		return this.presentAddress;
	}

	public void setPresentAddress(String presentAddress) {
		this.presentAddress = presentAddress;
	}

	public String getPresentAddressEn() {
		return this.presentAddressEn;
	}

	public void setPresentAddressEn(String presentAddressEn) {
		this.presentAddressEn = presentAddressEn;
	}

	public String getPermanentAddressEn() {
		return this.permanentAddressEn;
	}

	public void setPermanentAddressEn(String permanentAddressEn) {
		this.permanentAddressEn = permanentAddressEn;
	}

	public String getBirthPlace() {
		return this.birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getBirthPlaceEn() {
		return this.birthPlaceEn;
	}

	public void setBirthPlaceEn(String birthPlaceEn) {
		this.birthPlaceEn = birthPlaceEn;
	}

	public String getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getIssuanceDate() {
		return this.issuanceDate;
	}

	public void setIssuanceDate(Date issuanceDate) {
		this.issuanceDate = issuanceDate;
	}

	public String getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getTranslationId() {
		return this.translationId;
	}

	public void setTranslationId(String translationId) {
		this.translationId = translationId;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
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

	public Date getLastupdatedate() {
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

}
