package com.mfs.pricingprofile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_APP_USER_LOGIN_HISTORY database table.
 */
@Entity
@Table(name = "TBL_APP_USER_LOGIN_HISTORY")
@NamedQuery(name = "TblAppUserLoginHistory.findAll", query = "SELECT t FROM TblAppUserLoginHistory t")
public class TblAppUserLoginHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TBL_APP_USER_LOGIN_HISTORY_APPUSERLOGINHISTORYID_GENERATOR", sequenceName = "TBL_APP_USER_LOGIN_HISTORY_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_APP_USER_LOGIN_HISTORY_APPUSERLOGINHISTORYID_GENERATOR")
    @Column(name = "APP_USER_LOGIN_HISTORY_ID")
    private long appUserLoginHistoryId;

    @Column(name = "HOST")
    private String host;

    @Column(name = "IP_ADDRESS_A")
    private String ipAddressA;

    @Column(name = "IP_ADDRESS_P")
    private String ipAddressP;

    @Column(name = "LOGIN_DATE")
    private Date loginDate;

    @Column(name = "LOGOUT_DATE")
    private Date logoutDate;

    @Column(name = "MAC_ADDRESS")
    private BigDecimal macAddress;

    //bi-directional many-to-one association to TblAppUser
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "APP_USER_ID")
    private TblAppUser tblAppUser;

    public long getAppUserLoginHistoryId() {
        return this.appUserLoginHistoryId;
    }

    public void setAppUserLoginHistoryId(long appUserLoginHistoryId) {
        this.appUserLoginHistoryId = appUserLoginHistoryId;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIpAddressA() {
        return this.ipAddressA;
    }

    public void setIpAddressA(String ipAddressA) {
        this.ipAddressA = ipAddressA;
    }

    public String getIpAddressP() {
        return this.ipAddressP;
    }

    public void setIpAddressP(String ipAddressP) {
        this.ipAddressP = ipAddressP;
    }

    public Object getLoginDate() {
        return this.loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Date getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }

    public BigDecimal getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(BigDecimal macAddress) {
        this.macAddress = macAddress;
    }

    public TblAppUser getTblAppUser() {
        return this.tblAppUser;
    }

    public void setTblAppUser(TblAppUser tblAppUser) {
        this.tblAppUser = tblAppUser;
    }

}