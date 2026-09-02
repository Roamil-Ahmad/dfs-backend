package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the LKP_COUNTRY database table.
 * 
 */
@Entity
@Table(name="LKP_COUNTRY")
@NamedQuery(name="LkpCountry.findAll", query="SELECT l FROM LkpCountry l")
public class LkpCountry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_COUNTRY_COUNTRYID_GENERATOR", sequenceName="LKP_COUNTRY_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_COUNTRY_COUNTRYID_GENERATOR")
	@Column(name="COUNTRY_ID")
	private long countryId;

	@Column(name="COUNTRY_CODE")
	private String countryCode;

	@Column(name="COUNTRY_DESCR")
	private String countryDescr;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private String updateindex;

	@Column(name="ZONE")
	private String zone;

	//bi-directional many-to-one association to LkpCity
	@OneToMany(mappedBy="lkpCountry")
	private List<LkpCity> lkpCities;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	public LkpCountry() {
	}

	public long getCountryId() {
		return this.countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryDescr() {
		return this.countryDescr;
	}

	public void setCountryDescr(String countryDescr) {
		this.countryDescr = countryDescr;
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

	public String getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(String updateindex) {
		this.updateindex = updateindex;
	}

	public String getZone() {
		return this.zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public List<LkpCity> getLkpCities() {
		return this.lkpCities;
	}

	public void setLkpCities(List<LkpCity> lkpCities) {
		this.lkpCities = lkpCities;
	}

	public LkpCity addLkpCity(LkpCity lkpCity) {
		getLkpCities().add(lkpCity);
		lkpCity.setLkpCountry(this);

		return lkpCity;
	}

	public LkpCity removeLkpCity(LkpCity lkpCity) {
		getLkpCities().remove(lkpCity);
		lkpCity.setLkpCountry(null);

		return lkpCity;
	}

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

}