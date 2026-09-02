package com.dfs.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_TUTORIAL database table.
 */
@Entity
@Table(name = "TBL_TUTORIAL")
@NamedQuery(name = "TblTutorial.findAll", query = "SELECT t FROM TblTutorial t")
public class TblTutorial implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TBL_TUTORIAL_TUTORIALID_GENERATOR", sequenceName = "TBL_TUTORIAL_SEQ",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_TUTORIAL_TUTORIALID_GENERATOR")
    @Column(name = "TUTORIAL_ID")
    private long tutorialId;

    @JsonIgnore
    private Timestamp createdate;
    @JsonIgnore
    private BigDecimal createuser;

    @Column(name = "IS_ACTIVE")
    private String isActive;
    @JsonIgnore
    private Timestamp lastupdatedate;
    @JsonIgnore
    private BigDecimal lastupdateuser;

    @Column(name = "TUTORIAL_LINK")
    private String tutorialLink;

    @Column(name = "TITLE")
    private String title;
    @JsonIgnore
    private BigDecimal updateindex;

    public TblTutorial() {
    }

    public long getTutorialId() {
        return this.tutorialId;
    }

    public void setTutorialId(long tutorialId) {
        this.tutorialId = tutorialId;
    }

    public Timestamp getCreatedate() {
        return this.createdate;
    }

    public void setCreatedate(Timestamp createdate) {
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

    public Timestamp getLastupdatedate() {
        return this.lastupdatedate;
    }

    public void setLastupdatedate(Timestamp lastupdatedate) {
        this.lastupdatedate = lastupdatedate;
    }

    public BigDecimal getLastupdateuser() {
        return this.lastupdateuser;
    }

    public void setLastupdateuser(BigDecimal lastupdateuser) {
        this.lastupdateuser = lastupdateuser;
    }

    public String getTutorialLink() {
        return this.tutorialLink;
    }

    public void setTutorialLink(String tutorialLink) {
        this.tutorialLink = tutorialLink;
    }

    public BigDecimal getUpdateindex() {
        return this.updateindex;
    }

    public void setUpdateindex(BigDecimal updateindex) {
        this.updateindex = updateindex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}