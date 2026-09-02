package com.dfs.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_CMS_MENU database table.
 * 
 */
@Entity
@Table(name="TBL_CMS_MENU")
@NamedQuery(name="TblCmsMenu.findAll", query="SELECT t FROM TblCmsMenu t")
public class TblCmsMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CMS_MENU_CMSMENUID_GENERATOR", sequenceName="TBL_CMS_MENU_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CMS_MENU_CMSMENUID_GENERATOR")
	@Column(name="CMS_MENU_ID")
	private long cmsMenuId;

	@Column(name="CMS_MENU_CODE")
	private String cmsMenuCode;

	@Column(name="CMS_MENU_DESCR")
	private String cmsMenuDescr;

	@JsonIgnore
	private Timestamp createdate;

	@JsonIgnore
	private BigDecimal createuser;

	private String icon;

	@Column(name="IS_ACTIVE")
	@JsonIgnore
	private String isActive;

	@JsonIgnore
	private Timestamp lastupdatedate;

	@JsonIgnore
	private BigDecimal lastupdateuser;

	@Column(name="PARENT_CMS_MENU")
	private BigDecimal parentCmsMenu;

	@Column(name="SORT_SEQ")
	private String sortSeq;

	@Column(name="STATUS_ID")
	@JsonIgnore
	private BigDecimal statusId;

	@JsonIgnore
	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpCmsCategory
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="CMS_CATEGORY_ID")
	private LkpCmsCategory lkpCmsCategory;


	public long getCmsMenuId() {
		return this.cmsMenuId;
	}

	public void setCmsMenuId(long cmsMenuId) {
		this.cmsMenuId = cmsMenuId;
	}

	public String getCmsMenuCode() {
		return this.cmsMenuCode;
	}

	public void setCmsMenuCode(String cmsMenuCode) {
		this.cmsMenuCode = cmsMenuCode;
	}

	public String getCmsMenuDescr() {
		return this.cmsMenuDescr;
	}

	public void setCmsMenuDescr(String cmsMenuDescr) {
		this.cmsMenuDescr = cmsMenuDescr;
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

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public BigDecimal getParentCmsMenu() {
		return this.parentCmsMenu;
	}

	public void setParentCmsMenu(BigDecimal parentCmsMenu) {
		this.parentCmsMenu = parentCmsMenu;
	}

	public String getSortSeq() {
		return this.sortSeq;
	}

	public void setSortSeq(String sortSeq) {
		this.sortSeq = sortSeq;
	}

	public BigDecimal getStatusId() {
		return this.statusId;
	}

	public void setStatusId(BigDecimal statusId) {
		this.statusId = statusId;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public LkpCmsCategory getLkpCmsCategory() {
		return this.lkpCmsCategory;
	}

	public void setLkpCmsCategory(LkpCmsCategory lkpCmsCategory) {
		this.lkpCmsCategory = lkpCmsCategory;
	}

}