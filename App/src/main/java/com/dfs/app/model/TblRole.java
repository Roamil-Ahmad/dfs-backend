package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the TBL_ROLE database table.
 * 
 */
@Entity
@Table(name="TBL_ROLE")
@NamedQuery(name="TblRole.findAll", query="SELECT t FROM TblRole t")
public class TblRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_ROLE_ROLEID_GENERATOR", sequenceName="TBL_ROLE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_ROLE_ROLEID_GENERATOR")
	@Column(name="ROLE_ID")
	private long roleId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="ROLE_CODE")
	private String roleCode;

	@Column(name="ROLE_DESCR")
	private String roleDescr;

	private BigDecimal updateindex;


	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblRoleRight
	@OneToMany(mappedBy="tblRole")
	private List<TblRoleRight> tblRoleRights;

	//bi-directional many-to-one association to TblUserRole
	@OneToMany(mappedBy="tblRole")
	private List<TblUserRole> tblUserRoles;

	public TblRole() {
	}

	public long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
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

	public String getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleDescr() {
		return this.roleDescr;
	}

	public void setRoleDescr(String roleDescr) {
		this.roleDescr = roleDescr;
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
		tblRoleRight.setTblRole(this);

		return tblRoleRight;
	}

	public TblRoleRight removeTblRoleRight(TblRoleRight tblRoleRight) {
		getTblRoleRights().remove(tblRoleRight);
		tblRoleRight.setTblRole(null);

		return tblRoleRight;
	}

	public List<TblUserRole> getTblUserRoles() {
		return this.tblUserRoles;
	}

	public void setTblUserRoles(List<TblUserRole> tblUserRoles) {
		this.tblUserRoles = tblUserRoles;
	}

	public TblUserRole addTblUserRole(TblUserRole tblUserRole) {
		getTblUserRoles().add(tblUserRole);
		tblUserRole.setTblRole(this);

		return tblUserRole;
	}

	public TblUserRole removeTblUserRole(TblUserRole tblUserRole) {
		getTblUserRoles().remove(tblUserRole);
		tblUserRole.setTblRole(null);

		return tblUserRole;
	}

}