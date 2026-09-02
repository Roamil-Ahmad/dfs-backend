package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the LKP_REFERRED_BY database table.
 * 
 */
@Entity
@Table(name="LKP_REFERRED_BY")
@NamedQuery(name="LkpReferredBy.findAll", query="SELECT l FROM LkpReferredBy l")
public class LkpReferredBy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_REFERRED_BY_REFERREDBYID_GENERATOR", sequenceName="LKP_REFERRED_BY_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_REFERRED_BY_REFERREDBYID_GENERATOR")
	@Column(name="REFERRED_BY_ID")
	private long referredById;

	private String code;

	private Date createdate;

	private BigDecimal createuser;

	private String descr;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="SORT_SEQ")
	private BigDecimal sortSeq;

	private BigDecimal updateindex;

	public LkpReferredBy() {
	}

	public long getReferredById() {
		return this.referredById;
	}

	public void setReferredById(long referredById) {
		this.referredById = referredById;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
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

	public BigDecimal getSortSeq() {
		return this.sortSeq;
	}

	public void setSortSeq(BigDecimal sortSeq) {
		this.sortSeq = sortSeq;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}