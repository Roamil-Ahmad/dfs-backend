package com.workflow.modal;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
	@SequenceGenerator(name="TBL_USER_USERID_GENERATOR", sequenceName="TBL_USER_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_USER_USERID_GENERATOR")
	@Column(name="USER_ID")
	private long userId;

	@Column(name="NID_NO")
	private BigDecimal nidNo;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	private String department;

	private String designation;

	@Temporal(TemporalType.DATE)
	private Date dob;

	private String email;

	@Column(name="EMPLOYEE_NAME")
	private String employeeName;

	@Column(name="EMPLOYEE_NO")
	private BigDecimal employeeNo;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private String landline;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	@Column(name="GRANDFATHER_NAME")
	private String grandFatherName;

	@Column(name="PWD_UPDATE_FLAG")
	private String pwdUpdateFlag;

	@Column(name="STATUS_ID")
	private BigDecimal statusId;

	private BigDecimal updateindex;


	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public BigDecimal getNidNo() {
		return nidNo;
	}

	public void setNidNo(BigDecimal nidNo) {
		this.nidNo = nidNo;
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

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getGrandFatherName() {
		return grandFatherName;
	}

	public void setGrandFatherName(String grandFatherName) {
		this.grandFatherName = grandFatherName;
	}

	public String getPwdUpdateFlag() {
		return this.pwdUpdateFlag;
	}

	public void setPwdUpdateFlag(String pwdUpdateFlag) {
		this.pwdUpdateFlag = pwdUpdateFlag;
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

}