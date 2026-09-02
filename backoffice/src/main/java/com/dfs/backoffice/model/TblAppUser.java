package com.dfs.backoffice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_APP_USER database table.
 */
@Entity
@Table(name = "TBL_APP_USER")
@NamedQuery(name = "TblAppUser.findAll", query = "SELECT t FROM TblAppUser t")
public class TblAppUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TBL_APP_USER_APPUSERID_GENERATOR", sequenceName = "TBL_APP_USER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_APP_USER_APPUSERID_GENERATOR")
    @Column(name = "APP_USER_ID")
    private long appUserId;

    @Column(name = "AGENT_ID")
    private BigDecimal agentId;

    private Date createdate;

    private BigDecimal createuser;

    @Column(name = "CUSTOMER_ID")
    private BigDecimal customerId;

    @Column(name = "MOBILE_NO")
    private String mobileNo;

    private String password;

    @Column(name = "USER_ID")
    private BigDecimal userId;

    @Column(name = "USER_SMS")
    private String userSms;

    private String username;

    @Column(name = "PASSWORD_UPDATE_FLAG")
    private String passwordUpdateFlag;

    //bi-directional many-to-one association to TblAppUserLoginHistory
    @JsonIgnore
    @OneToMany(mappedBy = "tblAppUser")
    private List<TblAppUserLoginHistory> tblAppUserLoginHistories;

    public long getAppUserId() {
        return this.appUserId;
    }

    public void setAppUserId(long appUserId) {
        this.appUserId = appUserId;
    }

    public BigDecimal getAgentId() {
        return this.agentId;
    }

    public void setAgentId(BigDecimal agentId) {
        this.agentId = agentId;
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

    public BigDecimal getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(BigDecimal customerId) {
        this.customerId = customerId;
    }

    public String getMobileNo() {
        return this.mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getUserId() {
        return this.userId;
    }

    public void setUserId(BigDecimal userId) {
        this.userId = userId;
    }

    public String getUserSms() {
        return this.userSms;
    }

    public void setUserSms(String userSms) {
        this.userSms = userSms;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<TblAppUserLoginHistory> getTblAppUserLoginHistories() {
        return this.tblAppUserLoginHistories;
    }

    public void setTblAppUserLoginHistories(List<TblAppUserLoginHistory> tblAppUserLoginHistories) {
        this.tblAppUserLoginHistories = tblAppUserLoginHistories;
    }

    public TblAppUserLoginHistory addTblAppUserLoginHistory(TblAppUserLoginHistory tblAppUserLoginHistory) {
        getTblAppUserLoginHistories().add(tblAppUserLoginHistory);
        tblAppUserLoginHistory.setTblAppUser(this);

        return tblAppUserLoginHistory;
    }

    public TblAppUserLoginHistory removeTblAppUserLoginHistory(TblAppUserLoginHistory tblAppUserLoginHistory) {
        getTblAppUserLoginHistories().remove(tblAppUserLoginHistory);
        tblAppUserLoginHistory.setTblAppUser(null);

        return tblAppUserLoginHistory;
    }

    public String getPasswordUpdateFlag() {
        return passwordUpdateFlag;
    }

    public void setPasswordUpdateFlag(String passwordUpdateFlag) {
        this.passwordUpdateFlag = passwordUpdateFlag;
    }
}