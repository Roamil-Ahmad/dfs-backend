package com.workflow.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_MC_CONFIG database table.
 */
@Table(name = "TBL_MC_CONFIG")
@Entity(name = "TblMcConfig")
@NamedQuery(name = "TblMcConfig.findAll", query = "SELECT t FROM TblMcConfig t")
public class TblMcConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TBL_MC_CONFIG_MCCONFIGID_GENERATOR", sequenceName = "TBL_MC_CONFIG_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_MC_CONFIG_MCCONFIGID_GENERATOR")
    @Column(name = "MC_CONFIG_ID")
    private long mcConfigId;

    @Column(name = "CONFIG_DESCRIPTION")
    private String configDescription;

    @Column(name = "CONFIG_NAME")
    private String configName;

    @Temporal(TemporalType.DATE)
    private Date createdate;

    private BigDecimal createuser;

    @Column(name = "FORM_NAME")
    private String formName;

    @Column(name = "IS_ACTIVE")
    private String isActive;

    @Temporal(TemporalType.DATE)
    private Date lastupdatedate;

    private BigDecimal lastupdateuser;

    @Column(name = "REQUEST_TYPE")
    private String requestType;

    @Column(name = "TABLE_NAME")
    private String tableName;

    private BigDecimal updateindex;

    @Column(name = "EDIT_DETAIL_URL")
    private String editDetailUrl;

    @Column(name = "VIEW_DETAIL_URL")
    private String viewDetailUrl;

    private String comments;

    //bi-directional many-to-one association to TblMcConfigDetail
    @OneToMany(mappedBy = "tblMcConfig")
    private List<TblMcConfigDetail> tblMcConfigDetails;

    //bi-directional many-to-one association to LkpStatus
    @ManyToOne
    @JoinColumn(name = "STATUS_ID")
    private LkpStatus lkpStatus;

    public long getMcConfigId() {
        return this.mcConfigId;
    }

    public void setMcConfigId(long mcConfigId) {
        this.mcConfigId = mcConfigId;
    }

    public String getConfigDescription() {
        return configDescription;
    }

    public void setConfigDescription(String configDescription) {
        this.configDescription = configDescription;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
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

    public String getFormName() {
        return this.formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getIsActive() {
        return this.isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
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

    public String getRequestType() {
        return this.requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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
        tblMcConfigDetail.setTblMcConfig(this);

        return tblMcConfigDetail;
    }

    public TblMcConfigDetail removeTblMcConfigDetail(TblMcConfigDetail tblMcConfigDetail) {
        getTblMcConfigDetails().remove(tblMcConfigDetail);
        tblMcConfigDetail.setTblMcConfig(null);

        return tblMcConfigDetail;
    }

    public String getEditDetailUrl() {
        return editDetailUrl;
    }

    public void setEditDetailUrl(String editDetailUrl) {
        this.editDetailUrl = editDetailUrl;
    }

    public String getViewDetailUrl() {
        return viewDetailUrl;
    }

    public void setViewDetailUrl(String viewDetailUrl) {
        this.viewDetailUrl = viewDetailUrl;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LkpStatus getLkpStatus() {
        return lkpStatus;
    }

    public void setLkpStatus(LkpStatus lkpStatus) {
        this.lkpStatus = lkpStatus;
    }
}