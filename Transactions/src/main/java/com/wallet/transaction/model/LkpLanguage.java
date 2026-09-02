package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the LKP_LANGUAGE database table.
 * 
 */
@Entity
@Table(name="LKP_LANGUAGE")
@NamedQuery(name="LkpLanguage.findAll", query="SELECT l FROM LkpLanguage l")
public class LkpLanguage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_LANGUAGE_LANGUAGEID_GENERATOR", sequenceName="LKP_LANGUAGE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_LANGUAGE_LANGUAGEID_GENERATOR")
	@Column(name="LANGUAGE_ID")
	private long languageId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	@Column(name="IS_DEFAULT")
	private String isDefault;

	@Column(name="LANGUAGE_CODE")
	private String languageCode;

	@Column(name="LANGUAGE_DESCR")
	private String languageDescr;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblMultilanguage
	@OneToMany(mappedBy="lkpLanguage")
	private List<TblMultilanguage> tblMultilanguages;

	public LkpLanguage() {
	}

	public long getLanguageId() {
		return this.languageId;
	}

	public void setLanguageId(long languageId) {
		this.languageId = languageId;
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

	public String getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getLanguageCode() {
		return this.languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getLanguageDescr() {
		return this.languageDescr;
	}

	public void setLanguageDescr(String languageDescr) {
		this.languageDescr = languageDescr;
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

	public List<TblMultilanguage> getTblMultilanguages() {
		return this.tblMultilanguages;
	}

	public void setTblMultilanguages(List<TblMultilanguage> tblMultilanguages) {
		this.tblMultilanguages = tblMultilanguages;
	}

	public TblMultilanguage addTblMultilanguage(TblMultilanguage tblMultilanguage) {
		getTblMultilanguages().add(tblMultilanguage);
		tblMultilanguage.setLkpLanguage(this);

		return tblMultilanguage;
	}

	public TblMultilanguage removeTblMultilanguage(TblMultilanguage tblMultilanguage) {
		getTblMultilanguages().remove(tblMultilanguage);
		tblMultilanguage.setLkpLanguage(null);

		return tblMultilanguage;
	}

}