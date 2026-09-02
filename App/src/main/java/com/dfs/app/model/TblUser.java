package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the TBL_USER database table.
 * 
 */
@Entity
@Table(name="TBL_USER")
@NamedQuery(name="TblUser.findAll", query="SELECT t FROM TblUser t")
public class TblUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_USER_USERID_GENERATOR", sequenceName="TBL_USER_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_USER_USERID_GENERATOR")
	@Column(name="USER_ID")
	private long userId;

	private Date createdate;

	private BigDecimal createuser;

	private String department;

	private String designation;

	private Date dob;

	private String email;

	@Column(name="EMPLOYEE_NAME")
	private String employeeName;

	@Column(name="EMPLOYEE_NO")
	private BigDecimal employeeNo;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private String landline;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	@Column(name="PWD_UPDATE_FLAG")
	private String pwdUpdateFlag;

	@Column(name="NID_NO")
	private BigDecimal nidNo;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblMcConfigDetail
	@OneToMany(mappedBy="tblUser")
	private List<TblMcConfigDetail> tblMcConfigDetails;

	//bi-directional many-to-one association to TblMcNotification
	@OneToMany(mappedBy="tblUser")
	private List<TblMcNotification> tblMcNotifications;

	//bi-directional many-to-one association to TblMcPendingRequest
	@OneToMany(mappedBy="tblUser")
	private List<TblMcPendingRequest> tblMcPendingRequests;

	//bi-directional many-to-one association to TblMcRequest
	@OneToMany(mappedBy="tblUser")
	private List<TblMcRequest> tblMcRequests;

	//bi-directional many-to-one association to TblMcRequestAction
	@OneToMany(mappedBy="tblUser")
	private List<TblMcRequestAction> tblMcRequestActions;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblUserRole
	@OneToMany(mappedBy="tblUser")
	private List<TblUserRole> tblUserRoles;

	public TblUser() {
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Date getDob() {
		return this.dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmployeeName() {
		return this.employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public BigDecimal getEmployeeNo() {
		return this.employeeNo;
	}

	public void setEmployeeNo(BigDecimal employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getLandline() {
		return this.landline;
	}

	public void setLandline(String landline) {
		this.landline = landline;
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

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPwdUpdateFlag() {
		return this.pwdUpdateFlag;
	}

	public void setPwdUpdateFlag(String pwdUpdateFlag) {
		this.pwdUpdateFlag = pwdUpdateFlag;
	}

	public BigDecimal getNidNo() {
		return this.nidNo;
	}

	public void setNidNo(BigDecimal nidNo) {
		this.nidNo = nidNo;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public List<TblMcConfigDetail> getTblMcConfigDetails() {
		return this.tblMcConfigDetails;
	}

	public void setTblMcConfigDetails(List<TblMcConfigDetail> tblMcConfigDetails) {
		this.tblMcConfigDetails = tblMcConfigDetails;
	}

	public TblMcConfigDetail addTblMcConfigDetail(TblMcConfigDetail tblMcConfigDetail) {
		getTblMcConfigDetails().add(tblMcConfigDetail);
		tblMcConfigDetail.setTblUser(this);

		return tblMcConfigDetail;
	}

	public TblMcConfigDetail removeTblMcConfigDetail(TblMcConfigDetail tblMcConfigDetail) {
		getTblMcConfigDetails().remove(tblMcConfigDetail);
		tblMcConfigDetail.setTblUser(null);

		return tblMcConfigDetail;
	}

	public List<TblMcNotification> getTblMcNotifications() {
		return this.tblMcNotifications;
	}

	public void setTblMcNotifications(List<TblMcNotification> tblMcNotifications) {
		this.tblMcNotifications = tblMcNotifications;
	}

	public TblMcNotification addTblMcNotification(TblMcNotification tblMcNotification) {
		getTblMcNotifications().add(tblMcNotification);
		tblMcNotification.setTblUser(this);

		return tblMcNotification;
	}

	public TblMcNotification removeTblMcNotification(TblMcNotification tblMcNotification) {
		getTblMcNotifications().remove(tblMcNotification);
		tblMcNotification.setTblUser(null);

		return tblMcNotification;
	}

	public List<TblMcPendingRequest> getTblMcPendingRequests() {
		return this.tblMcPendingRequests;
	}

	public void setTblMcPendingRequests(List<TblMcPendingRequest> tblMcPendingRequests) {
		this.tblMcPendingRequests = tblMcPendingRequests;
	}

	public TblMcPendingRequest addTblMcPendingRequest(TblMcPendingRequest tblMcPendingRequest) {
		getTblMcPendingRequests().add(tblMcPendingRequest);
		tblMcPendingRequest.setTblUser(this);

		return tblMcPendingRequest;
	}

	public TblMcPendingRequest removeTblMcPendingRequest(TblMcPendingRequest tblMcPendingRequest) {
		getTblMcPendingRequests().remove(tblMcPendingRequest);
		tblMcPendingRequest.setTblUser(null);

		return tblMcPendingRequest;
	}

	public List<TblMcRequest> getTblMcRequests() {
		return this.tblMcRequests;
	}

	public void setTblMcRequests(List<TblMcRequest> tblMcRequests) {
		this.tblMcRequests = tblMcRequests;
	}

	public TblMcRequest addTblMcRequest(TblMcRequest tblMcRequest) {
		getTblMcRequests().add(tblMcRequest);
		tblMcRequest.setTblUser(this);

		return tblMcRequest;
	}

	public TblMcRequest removeTblMcRequest(TblMcRequest tblMcRequest) {
		getTblMcRequests().remove(tblMcRequest);
		tblMcRequest.setTblUser(null);

		return tblMcRequest;
	}

	public List<TblMcRequestAction> getTblMcRequestActions() {
		return this.tblMcRequestActions;
	}

	public void setTblMcRequestActions(List<TblMcRequestAction> tblMcRequestActions) {
		this.tblMcRequestActions = tblMcRequestActions;
	}

	public TblMcRequestAction addTblMcRequestAction(TblMcRequestAction tblMcRequestAction) {
		getTblMcRequestActions().add(tblMcRequestAction);
		tblMcRequestAction.setTblUser(this);

		return tblMcRequestAction;
	}

	public TblMcRequestAction removeTblMcRequestAction(TblMcRequestAction tblMcRequestAction) {
		getTblMcRequestActions().remove(tblMcRequestAction);
		tblMcRequestAction.setTblUser(null);

		return tblMcRequestAction;
	}

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

	public List<TblUserRole> getTblUserRoles() {
		return this.tblUserRoles;
	}

	public void setTblUserRoles(List<TblUserRole> tblUserRoles) {
		this.tblUserRoles = tblUserRoles;
	}

	public TblUserRole addTblUserRole(TblUserRole tblUserRole) {
		getTblUserRoles().add(tblUserRole);
		tblUserRole.setTblUser(this);

		return tblUserRole;
	}

	public TblUserRole removeTblUserRole(TblUserRole tblUserRole) {
		getTblUserRoles().remove(tblUserRole);
		tblUserRole.setTblUser(null);

		return tblUserRole;
	}

}