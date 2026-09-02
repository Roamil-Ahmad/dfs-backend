package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_MENU database table.
 * 
 */
@Entity
@Table(name="TBL_MENU")
@NamedQuery(name="TblMenu.findAll", query="SELECT t FROM TblMenu t")
public class TblMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_MENU_MENUID_GENERATOR", sequenceName="TBL_MENU_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MENU_MENUID_GENERATOR")
	@Column(name="MENU_ID")
	private long menuId;

	private Date createdate;

	private BigDecimal createuser;

	private String icon;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MENU_CODE")
	private String menuCode;

	@Column(name="MENU_DESCR")
	private String menuDescr;

	@Column(name="MENU_PATH")
	private String menuPath;

	@Column(name="MENU_TYPE")
	private String menuType;

	@Column(name="PARENT_MENU")
	private BigDecimal parentMenu;

	@Column(name="SORT_SEQ")
	private String sortSeq;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblRoleRight
	@OneToMany(mappedBy="tblMenu")
	private List<TblRoleRight> tblRoleRights;

	public TblMenu() {
	}

	public long getMenuId() {
		return this.menuId;
	}

	public void setMenuId(long menuId) {
		this.menuId = menuId;
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

	public String getMenuCode() {
		return this.menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuDescr() {
		return this.menuDescr;
	}

	public void setMenuDescr(String menuDescr) {
		this.menuDescr = menuDescr;
	}

	public String getMenuPath() {
		return this.menuPath;
	}

	public void setMenuPath(String menuPath) {
		this.menuPath = menuPath;
	}

	public String getMenuType() {
		return this.menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public BigDecimal getParentMenu() {
		return this.parentMenu;
	}

	public void setParentMenu(BigDecimal parentMenu) {
		this.parentMenu = parentMenu;
	}

	public String getSortSeq() {
		return this.sortSeq;
	}

	public void setSortSeq(String sortSeq) {
		this.sortSeq = sortSeq;
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

	public List<TblRoleRight> getTblRoleRights() {
		return this.tblRoleRights;
	}

	public void setTblRoleRights(List<TblRoleRight> tblRoleRights) {
		this.tblRoleRights = tblRoleRights;
	}

	public TblRoleRight addTblRoleRight(TblRoleRight tblRoleRight) {
		getTblRoleRights().add(tblRoleRight);
		tblRoleRight.setTblMenu(this);

		return tblRoleRight;
	}

	public TblRoleRight removeTblRoleRight(TblRoleRight tblRoleRight) {
		getTblRoleRights().remove(tblRoleRight);
		tblRoleRight.setTblMenu(null);

		return tblRoleRight;
	}

}