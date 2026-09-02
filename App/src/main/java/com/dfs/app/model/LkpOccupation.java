package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the LKP_OCCUPATION database table.
 * 
 */
@Entity
@Table(name="LKP_OCCUPATION")
@NamedQuery(name="LkpOccupation.findAll", query="SELECT l FROM LkpOccupation l")
public class LkpOccupation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_OCCUPATION_OCCUPATIONID_GENERATOR", sequenceName="LKP_OCCUPATION_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_OCCUPATION_OCCUPATIONID_GENERATOR")
	@Column(name="OCCUPATION_ID")
	private long occupationId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="OCCUPATION_CODE")
	private String occupationCode;

	@Column(name="OCCUPATION_DESCR")
	private String occupationDescr;

	@Column(name="OCCUPATION_NAME")
	private String occupationName;

	@Column(name="RP_RATING")
	private String rpRating;

	private BigDecimal updateindex;

	public LkpOccupation() {
	}

	public long getOccupationId() {
		return this.occupationId;
	}

	public void setOccupationId(long occupationId) {
		this.occupationId = occupationId;
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

	public String getOccupationCode() {
		return this.occupationCode;
	}

	public void setOccupationCode(String occupationCode) {
		this.occupationCode = occupationCode;
	}

	public String getOccupationDescr() {
		return this.occupationDescr;
	}

	public void setOccupationDescr(String occupationDescr) {
		this.occupationDescr = occupationDescr;
	}

	public String getOccupationName() {
		return this.occupationName;
	}

	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
	}

	public String getRpRating() {
		return this.rpRating;
	}

	public void setRpRating(String rpRating) {
		this.rpRating = rpRating;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}