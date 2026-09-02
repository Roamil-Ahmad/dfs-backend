package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_ROLE_RIGHTS database table.
 * 
 */
@Entity
@Table(name="TBL_ROLE_RIGHTS")
@NamedQuery(name="TblRoleRight.findAll", query="SELECT t FROM TblRoleRight t")
public class TblRoleRight implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_ROLE_RIGHTS_ROLERIGHTSID_GENERATOR", sequenceName="TBL_ROLE_RIGHTS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_ROLE_RIGHTS_ROLERIGHTSID_GENERATOR")
	@Column(name="ROLE_RIGHTS_ID")
	private long roleRightsId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DELETE_ALLOWED")
	private String deleteAllowed;

	@Column(name="INSERT_ALLOWED")
	private String insertAllowed;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="UPDATE_ALLOWED")
	private String updateAllowed;

	private BigDecimal updateindex;

	@Column(name="VIEW_ALLOWED")
	private String viewAllowed;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblMenu
	@ManyToOne
	@JoinColumn(name="MENU_ID")
	private TblMenu tblMenu;

	//bi-directional many-to-one association to TblRole
	@ManyToOne
	@JoinColumn(name="ROLE_ID")
	private TblRole tblRole;

	public TblRoleRight() {
	}

	public long getRoleRightsId() {
		return this.roleRightsId;
	}

	public void setRoleRightsId(long roleRightsId) {
		this.roleRightsId = roleRightsId;
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

	public String getDeleteAllowed() {
		return this.deleteAllowed;
	}

	public void setDeleteAllowed(String deleteAllowed) {
		this.deleteAllowed = deleteAllowed;
	}

	public String getInsertAllowed() {
		return this.insertAllowed;
	}

	public void setInsertAllowed(String insertAllowed) {
		this.insertAllowed = insertAllowed;
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

	public String getUpdateAllowed() {
		return this.updateAllowed;
	}

	public void setUpdateAllowed(String updateAllowed) {
		this.updateAllowed = updateAllowed;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public String getViewAllowed() {
		return this.viewAllowed;
	}

	public void setViewAllowed(String viewAllowed) {
		this.viewAllowed = viewAllowed;
	}

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

	public TblMenu getTblMenu() {
		return this.tblMenu;
	}

	public void setTblMenu(TblMenu tblMenu) {
		this.tblMenu = tblMenu;
	}

	public TblRole getTblRole() {
		return this.tblRole;
	}

	public void setTblRole(TblRole tblRole) {
		this.tblRole = tblRole;
	}

}