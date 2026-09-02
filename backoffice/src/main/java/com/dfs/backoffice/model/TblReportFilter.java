package com.dfs.backoffice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_REPORT_FILTER database table.
 * 
 */
@Entity
@Table(name="TBL_REPORT_FILTER")
@NamedQuery(name="TblReportFilter.findAll", query="SELECT t FROM TblReportFilter t")
public class TblReportFilter implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_REPORT_FILTER_REPORTFILTERID_GENERATOR", sequenceName="TBL_REPORT_FILTER_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_REPORT_FILTER_REPORTFILTERID_GENERATOR")
	@Column(name="REPORT_FILTER_ID")
	private long reportFilterId;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MAPPED_FILTER_NAME")
	private String mappedFilterName;

	@Column(name="REPORT_FILTER_NAME")
	private String reportFilterName;

	@Column(name="REPORT_FILTER_TYPE")
	private String reportFilterType;

	@Column(name="REPORT_FILTER_URL")
	private String reportFilterUrl;

	private BigDecimal updateindex;

	@Column(name="IS_MANDATORY")
	private String isMandatory;

	//bi-directional many-to-one association to TblReport
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="REPORT_ID")
	private TblReport tblReport;

	public long getReportFilterId() {
		return this.reportFilterId;
	}

	public void setReportFilterId(long reportFilterId) {
		this.reportFilterId = reportFilterId;
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

	public String getMappedFilterName() {
		return this.mappedFilterName;
	}

	public void setMappedFilterName(String mappedFilterName) {
		this.mappedFilterName = mappedFilterName;
	}

	public String getReportFilterName() {
		return this.reportFilterName;
	}

	public void setReportFilterName(String reportFilterName) {
		this.reportFilterName = reportFilterName;
	}

	public String getReportFilterType() {
		return this.reportFilterType;
	}

	public void setReportFilterType(String reportFilterType) {
		this.reportFilterType = reportFilterType;
	}

	public String getReportFilterUrl() {
		return this.reportFilterUrl;
	}

	public void setReportFilterUrl(String reportFilterUrl) {
		this.reportFilterUrl = reportFilterUrl;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public TblReport getTblReport() {
		return tblReport;
	}

	public void setTblReport(TblReport tblReport) {
		this.tblReport = tblReport;
	}

	public String getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(String isMandatory) {
		this.isMandatory = isMandatory;
	}
}