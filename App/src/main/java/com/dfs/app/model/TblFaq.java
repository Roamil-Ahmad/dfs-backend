package com.dfs.app.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_FAQ database table.
 */
@Entity
@Table(name = "TBL_FAQ")
@NamedQuery(name = "TblFaq.findAll", query = "SELECT t FROM TblFaq t")
public class TblFaq implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TBL_FAQ_FAQID_GENERATOR", sequenceName = "TBL_FAQ_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_FAQ_FAQID_GENERATOR")
    @Column(name = "FAQ_ID")
    @JsonIgnore
    private long faqId;

    private String question;

    private String answer;

    @JsonIgnore
    private Timestamp createdate;
    @JsonIgnore
    private BigDecimal createuser;

    @Column(name = "IS_ACTIVE")
    @JsonIgnore
    private String isActive;
    @JsonIgnore
    private Timestamp lastupdatedate;
    @JsonIgnore
    private BigDecimal lastupdateuser;

    @JsonIgnore
    private BigDecimal updateindex;

    //bi-directional many-to-one association to LkpFaqCategory
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "FAQ_CATEGORY_ID")
    private LkpFaqCategory lkpFaqCategory;

    public TblFaq() {
    }

    public long getFaqId() {
        return this.faqId;
    }

    public void setFaqId(long faqId) {
        this.faqId = faqId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public BigDecimal getUpdateindex() {
        return this.updateindex;
    }

    public void setUpdateindex(BigDecimal updateindex) {
        this.updateindex = updateindex;
    }

    public LkpFaqCategory getLkpFaqCategory() {
        return lkpFaqCategory;
    }

    public void setLkpFaqCategory(LkpFaqCategory lkpFaqCategory) {
        this.lkpFaqCategory = lkpFaqCategory;
    }
}