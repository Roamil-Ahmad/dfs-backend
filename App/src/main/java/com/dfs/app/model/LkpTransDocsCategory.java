package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the LKP_TRANS_DOCS_CATEGORY database table.
 * 
 */
@Entity
@Table(name="LKP_TRANS_DOCS_CATEGORY")
@NamedQuery(name="LkpTransDocsCategory.findAll", query="SELECT l FROM LkpTransDocsCategory l")
public class LkpTransDocsCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_TRANS_DOCS_CATEGORY_TRANSDOCSCATEGORYID_GENERATOR", sequenceName="LKP_TRANS_DOCS_CATEGORY_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_TRANS_DOCS_CATEGORY_TRANSDOCSCATEGORYID_GENERATOR")
	@Column(name="TRANS_DOCS_CATEGORY_ID")
	private long transDocsCategoryId;

	@Column(name="CATEGORY_CODE")
	private String categoryCode;

	@Column(name="CATEGORY_DESCR")
	private String categoryDescr;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	public LkpTransDocsCategory() {
	}

	public long getTransDocsCategoryId() {
		return this.transDocsCategoryId;
	}

	public void setTransDocsCategoryId(long transDocsCategoryId) {
		this.transDocsCategoryId = transDocsCategoryId;
	}

	public String getCategoryCode() {
		return this.categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryDescr() {
		return this.categoryDescr;
	}

	public void setCategoryDescr(String categoryDescr) {
		this.categoryDescr = categoryDescr;
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

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}