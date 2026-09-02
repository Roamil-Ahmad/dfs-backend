package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the LKP_MC_FORM_NAME database table.
 * 
 */
@Entity
@Table(name="LKP_MC_FORM_NAME")
@NamedQuery(name="LkpMcFormName.findAll", query="SELECT l FROM LkpMcFormName l")
public class LkpMcFormName implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_MC_FORM_NAME_MCFORMNAMEID_GENERATOR", sequenceName="LKP_MC_FORM_NAME_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_MC_FORM_NAME_MCFORMNAMEID_GENERATOR")
	@Column(name="MC_FORM_NAME_ID")
	private long mcFormNameId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="EDIT_DETAIL_URL")
	private String editDetailUrl;

	@Column(name="FORM_NAME")
	private String formName;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="REQUEST_TYPE")
	private String requestType;

	@Column(name="TABLE_NAME")
	private String tableName;

	private BigDecimal updateindex;

	@Column(name="VIEW_DETAIL_URL")
	private String viewDetailUrl;

	public LkpMcFormName() {
	}

	public long getMcFormNameId() {
		return this.mcFormNameId;
	}

	public void setMcFormNameId(long mcFormNameId) {
		this.mcFormNameId = mcFormNameId;
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

	public String getEditDetailUrl() {
		return this.editDetailUrl;
	}

	public void setEditDetailUrl(String editDetailUrl) {
		this.editDetailUrl = editDetailUrl;
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

	public String getViewDetailUrl() {
		return this.viewDetailUrl;
	}

	public void setViewDetailUrl(String viewDetailUrl) {
		this.viewDetailUrl = viewDetailUrl;
	}

}