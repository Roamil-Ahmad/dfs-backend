package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_NADRA_MISMATCH database table.
 * 
 */
@Entity
@Table(name="TBL_NADRA_MISMATCH")
@NamedQuery(name="TblNadraMismatch.findAll", query="SELECT t FROM TblNadraMismatch t")
public class TblNadraMismatch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_NADRA_MISMATCH_NADRAMISMATCHID_GENERATOR", sequenceName="TBL_NADRA_MISMATCH_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_NADRA_MISMATCH_NADRAMISMATCHID_GENERATOR")
	@Column(name="NADRA_MISMATCH_ID")
	private long nadraMismatchId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DATE_OF_BIRTH")
	private String dateOfBirth;

	@Column(name="EXPIRY_DATE")
	private String expiryDate;

	@Column(name="FATHER_HUSBAND_NAME_EN")
	private String fatherHusbandNameEn;

	@Column(name="MOTHER_NAME_EN")
	private String motherNameEn;

	@Column(name="NAME_EN")
	private String nameEn;

	private String reason;

	//bi-directional many-to-one association to TblNadra
	@ManyToOne
	@JoinColumn(name="CNIC")
	private TblNadra tblNadra;

	public TblNadraMismatch() {
	}

	public long getNadraMismatchId() {
		return this.nadraMismatchId;
	}

	public void setNadraMismatchId(long nadraMismatchId) {
		this.nadraMismatchId = nadraMismatchId;
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

	public String getMotherNameEn() {
		return this.motherNameEn;
	}

	public void setMotherNameEn(String motherNameEn) {
		this.motherNameEn = motherNameEn;
	}

	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public TblNadra getTblNadra() {
		return this.tblNadra;
	}

	public void setTblNadra(TblNadra tblNadra) {
		this.tblNadra = tblNadra;
	}

}