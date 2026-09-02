package com.dfs.backoffice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_USER database table.
 */
@Entity
@Table(name = "TBL_USER")
@NamedQuery(name = "TblUser.findAll", query = "SELECT t FROM TblUser t")
public class TblUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TBL_USER_USERID_GENERATOR", sequenceName = "TBL_USER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_USER_USERID_GENERATOR")
    @Column(name = "USER_ID")
    private long userId;

    private Date createdate;

    private BigDecimal createuser;

    private String department;

    private String designation;

    @Temporal(TemporalType.DATE)
    private Date dob;

    private String email;

    @Column(name = "EMPLOYEE_NAME")
    private String employeeName;

    @Column(name = "EMPLOYEE_NO")
    private String employeeNo;

    @Column(name = "GRANDFATHER_NAME")
    private String grandfatherName;

    @Column(name = "IS_ACTIVE")
    private String isActive;

    private String landline;

    private Date lastupdatedate;

    private BigDecimal lastupdateuser;

    @Column(name = "MOBILE_NO")
    private String mobileNo;

    @Column(name="NID_NO")
    private BigDecimal nidNo;

    private BigDecimal updateindex;

    //bi-directional many-to-one association to LkpStatus
    @ManyToOne
    @JoinColumn(name = "STATUS_ID")
    private LkpStatus lkpStatus;

    //bi-directional many-to-one association to TblUserRole
    @OneToMany(mappedBy = "tblUser")
    private List<TblUserRole> tblUserRoles;

    @Transient
    private String username;

    @Column(name = "TWO_FA_ENABLED")
    private String twoFaEnabled;

    @Column(name = "TWO_FA_TYPE")
    private String twoFaType;

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getCreatedate() {
        return createdate;
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

    public String getEmployeeNo() {
        return this.employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getGrandfatherName() {
        return this.grandfatherName;
    }

    public void setGrandfatherName(String grandfatherName) {
        this.grandfatherName = grandfatherName;
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
        return lastupdatedate;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTwoFaEnabled() {
        return twoFaEnabled;
    }

    public void setTwoFaEnabled(String twoFaEnabled) {
        this.twoFaEnabled = twoFaEnabled;
    }

    public String getTwoFaType() {
        return twoFaType;
    }

    public void setTwoFaType(String twoFaType) {
        this.twoFaType = twoFaType;
    }
}