package com.dfs.backoffice.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_MESSAGE database table.
 */
@Entity
@Table(name = "TBL_MESSAGE")
@NamedQuery(name = "TblMessage.findAll", query = "SELECT t FROM TblMessage t")
public class TblMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TBL_MESSAGE_MESSAGEID_GENERATOR", sequenceName = "TBL_MESSAGE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_MESSAGE_MESSAGEID_GENERATOR")
    @Column(name = "MESSAGE_ID")
    private long messageId;

    private Date createdate;

    private BigDecimal createuser;

    @Column(name = "IS_ACTIVE")
    private String isActive;

    private Date lastupdatedate;

    private BigDecimal lastupdateuser;

    @Column(name = "MESSAGE_CODE")
    private String messageCode;

    @Column(name = "MESSAGE_DESCR")
    private String messageDescr;

    private BigDecimal updateindex;

    //bi-directional many-to-one association to TblAppUserActivityLog
    @OneToMany(mappedBy = "tblMessage")
    private List<TblAppUserActivityLog> tblAppUserActivityLogs;

    //bi-directional many-to-one association to LkpMessageType
    @ManyToOne
    @JoinColumn(name = "MESSAGE_TYPE_ID")
    private LkpMessageType lkpMessageType;

    //bi-directional many-to-one association to LkpStatus
    @ManyToOne
    @JoinColumn(name = "STATUS_ID")
    private LkpStatus lkpStatus;

    public TblMessage() {
    }

    public long getMessageId() {
        return this.messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
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

    public String getMessageCode() {
        return this.messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessageDescr() {
        return this.messageDescr;
    }

    public void setMessageDescr(String messageDescr) {
        this.messageDescr = messageDescr;
    }

    public BigDecimal getUpdateindex() {
        return this.updateindex;
    }

    public void setUpdateindex(BigDecimal updateindex) {
        this.updateindex = updateindex;
    }

    public List<TblAppUserActivityLog> getTblAppUserActivityLogs() {
        return this.tblAppUserActivityLogs;
    }

    public void setTblAppUserActivityLogs(List<TblAppUserActivityLog> tblAppUserActivityLogs) {
        this.tblAppUserActivityLogs = tblAppUserActivityLogs;
    }

    public TblAppUserActivityLog addTblAppUserActivityLog(TblAppUserActivityLog tblAppUserActivityLog) {
        getTblAppUserActivityLogs().add(tblAppUserActivityLog);
        tblAppUserActivityLog.setTblMessage(this);

        return tblAppUserActivityLog;
    }

    public TblAppUserActivityLog removeTblAppUserActivityLog(TblAppUserActivityLog tblAppUserActivityLog) {
        getTblAppUserActivityLogs().remove(tblAppUserActivityLog);
        tblAppUserActivityLog.setTblMessage(null);

        return tblAppUserActivityLog;
    }

    public LkpMessageType getLkpMessageType() {
        return this.lkpMessageType;
    }

    public void setLkpMessageType(LkpMessageType lkpMessageType) {
        this.lkpMessageType = lkpMessageType;
    }

    public LkpStatus getLkpStatus() {
        return this.lkpStatus;
    }

    public void setLkpStatus(LkpStatus lkpStatus) {
        this.lkpStatus = lkpStatus;
    }

}