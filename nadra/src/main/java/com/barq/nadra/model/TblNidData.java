package com.barq.nadra.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_NID_DATA database table.
 * 
 */
@Entity
@Table(name="TBL_NID_DATA")
@NamedQuery(name="TblNidData.findAll", query="SELECT t FROM TblNidData t")
public class TblNidData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="NID_NO")
	private long nidNo;

	@Column(name="TRANSLATION_ID")
	private String translationId;

	@Column(name="BIRTH_PLACE")
	private String birthPlace;

	@Column(name="BIRTH_PLACE_EN")
	private String birthPlaceEn;

	@Column(name="CARD_TYPE")
	private String cardType;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DATE_OF_BIRTH")
	private String dateOfBirth;

	private String dob;

	@Column(name="EXPIRY_DATE")
	private String expiryDate;

	@Column(name="FATHER_HUSBAND_NAME_EN")
	private String fatherHusbandNameEn;

	@Column(name="FATHER_NAME")
	private String fatherName;

	private String gender;

	private BigDecimal id;

	@Temporal(TemporalType.DATE)
	@Column(name="ISSUANCE_DATE")
	private Date issuanceDate;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MOTHER_NAME")
	private String motherName;

	@Column(name="MOTHER_NAME_EN")
	private String motherNameEn;

	private String name;

	@Column(name="NAME_EN")
	private String nameEn;

	private String nationality;

	@Column(name="PERMANENT_ADDRESS")
	private String permanentAddress;

	@Column(name="PERMANENT_ADDRESS_EN")
	private String permanentAddressEn;

	@Column(name="PRESENT_ADDRESS")
	private String presentAddress;

	@Column(name="PRESENT_ADDRESS_EN")
	private String presentAddressEn;

	@Column(name="RESIDENCE_COUNTRY")
	private String residenceCountry;

	@Column(name="SESSION_ID")
	private String sessionId;

	@Column(name="STATUS_CODE")
	private String statusCode;

	@Column(name="STATUS_MESSAGE")
	private String statusMessage;

	private BigDecimal updateindex;

	public TblNidData() {
	}

	public long getNidNo() {
		return this.nidNo;
	}

	public void setNidNo(long nidNo) {
		this.nidNo = nidNo;
	}

	public String getTranslationId() {
		return this.translationId;
	}

	public void setTranslationId(String translationId) {
		this.translationId = translationId;
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

	public String getCardType() {
		return this.cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
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

	public String getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDob() {
		return this.dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getFatherHusbandNameEn() {
		return this.fatherHusbandNameEn;
	}

	public void setFatherHusbandNameEn(String fatherHusbandNameEn) {
		this.fatherHusbandNameEn = fatherHusbandNameEn;
	}

	public String getFatherName() {
		return this.fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public Date getIssuanceDate() {
		return this.issuanceDate;
	}

	public void setIssuanceDate(Date issuanceDate) {
		this.issuanceDate = issuanceDate;
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

	public String getNationality() {
		return this.nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getPermanentAddress() {
		return this.permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public String getPermanentAddressEn() {
		return this.permanentAddressEn;
	}

	public void setPermanentAddressEn(String permanentAddressEn) {
		this.permanentAddressEn = permanentAddressEn;
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

	public String getResidenceCountry() {
		return this.residenceCountry;
	}

	public void setResidenceCountry(String residenceCountry) {
		this.residenceCountry = residenceCountry;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}