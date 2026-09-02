package com.mfs.pricingprofile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the TBL_SMS_MESSAGE_TEMPLATE database table.
 */
@Entity
@Table(name = "TBL_SMS_MESSAGE_TEMPLATE")
@NamedQuery(name = "TblSmsMessageTemplate.findAll", query = "SELECT t FROM TblSmsMessageTemplate t")
public class TblSmsMessageTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TBL_SMS_MESSAGE_TEMPLATE_SMSMESSAGETEMPLATEID_GENERATOR", sequenceName = "TBL_SMS_MESSAGE_TEMPLATE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_SMS_MESSAGE_TEMPLATE_SMSMESSAGETEMPLATEID_GENERATOR")
    @Column(name = "SMS_MESSAGE_TEMPLATE_ID")
    private long smsMessageTemplateId;

    // //
    @JsonIgnore
    private Date createdate;

    @JsonIgnore
    private BigDecimal createuser;

    private String idntifier;

    // //
    @JsonIgnore
    private Date lastupdatedate;

    @JsonIgnore
    private BigDecimal lastupdateuser;

    @Column(name = "MESSAGE_TEMPLATE")
    private String messageTemplate;

    @JsonIgnore
    private BigDecimal updateindex;

    // bi-directional many-to-one association to TblTransDoc
    @ManyToOne
    @JoinColumn(name = "TRANS_DOCS_ID")
    private TblTransDoc tblTransDoc;

    public TblSmsMessageTemplate() {
        this.tblTransDoc = new TblTransDoc();
    }

    public long getSmsMessageTemplateId() {
        return this.smsMessageTemplateId;
    }

    public void setSmsMessageTemplateId(long smsMessageTemplateId) {
        this.smsMessageTemplateId = smsMessageTemplateId;
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

    public String getIdntifier() {
        return this.idntifier;
    }

    public void setIdntifier(String idntifier) {
        this.idntifier = idntifier;
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

    public String getMessageTemplate() {
        return this.messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public BigDecimal getUpdateindex() {
        return this.updateindex;
    }

    public void setUpdateindex(BigDecimal updateindex) {
        this.updateindex = updateindex;
    }

    public TblTransDoc getTblTransDoc() {
        return this.tblTransDoc;
    }

    public void setTblTransDoc(TblTransDoc tblTransDoc) {
        this.tblTransDoc = tblTransDoc;
    }

}