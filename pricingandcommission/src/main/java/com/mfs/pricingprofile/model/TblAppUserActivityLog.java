package com.mfs.pricingprofile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the TBL_APP_USER_ACTIVITY_LOG database table.
 */
@Entity
@Table(name = "TBL_APP_USER_ACTIVITY_LOG")
@NamedQuery(name = "TblAppUserActivityLog.findAll", query = "SELECT t FROM TblAppUserActivityLog t")
public class TblAppUserActivityLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TBL_APP_USER_ACTIVITY_LOG_APPUSERACTIVITYLOGID_GENERATOR", sequenceName = "TBL_APP_USER_ACTIVITY_LOG_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_APP_USER_ACTIVITY_LOG_APPUSERACTIVITYLOGID_GENERATOR")
    @Column(name = "APP_USER_ACTIVITY_LOG_ID")
    private long appUserActivityLogId;

    @Column(name = "ACTIVITY_DATE")
    private Date activityDate;

    //bi-directional many-to-one association to TblAppUserLoginHistory
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "APP_USER_LOGIN_HISTORY_ID")
    private TblAppUserLoginHistory tblAppUserLoginHistory;

    //bi-directional many-to-one association to TblMessage
    @ManyToOne
    @JoinColumn(name = "MESSAGE_ID")
    private TblMessage tblMessage;

    //bi-directional many-to-one association to TblTransDoc
    @ManyToOne
    @JoinColumn(name = "TRANS_DOCS_ID")
    private TblTransDoc tblTransDoc;

    public long getAppUserActivityLogId() {
        return this.appUserActivityLogId;
    }

    public void setAppUserActivityLogId(long appUserActivityLogId) {
        this.appUserActivityLogId = appUserActivityLogId;
    }

    public Object getActivityDate() {
        return this.activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public TblAppUserLoginHistory getTblAppUserLoginHistory() {
        return this.tblAppUserLoginHistory;
    }

    public void setTblAppUserLoginHistory(TblAppUserLoginHistory tblAppUserLoginHistory) {
        this.tblAppUserLoginHistory = tblAppUserLoginHistory;
    }

    public TblMessage getTblMessage() {
        return this.tblMessage;
    }

    public void setTblMessage(TblMessage tblMessage) {
        this.tblMessage = tblMessage;
    }

    public TblTransDoc getTblTransDoc() {
        return this.tblTransDoc;
    }

    public void setTblTransDoc(TblTransDoc tblTransDoc) {
        this.tblTransDoc = tblTransDoc;
    }

}