package com.dfs.backoffice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the TBL_REPORT database table.
 * 
 */
@Entity
@Table(name="TBL_REPORT")
@NamedQuery(name="TblReport.findAll", query="SELECT t FROM TblReport t")
public class TblReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_REPORT_REPORTID_GENERATOR", sequenceName="TBL_REPORT_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_REPORT_REPORTID_GENERATOR")
	@Column(name="REPORT_ID")
	private long reportId;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="FILE_NAME")
	private String fileName;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="REPORT_DESCR")
	private String reportDescr;

	@Column(name="REPORT_NAME")
	private String reportName;

	@Column(name="REPORT_VIEW")
	private String reportView;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblReportFilter
	@JsonIgnore
	@OneToMany(mappedBy="tblReport")
	private List<TblReportFilter> tblReportFilters;

	//bi-directional many-to-one association to TblReportQuery
	@JsonIgnore
	@OneToMany(mappedBy="tblReport")
	private List<TblReportQuery> tblReportQueries;

	public long getReportId() {
		return this.reportId;
	}

	public void setReportId(long reportId) {
		this.reportId = reportId;
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

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public String getReportDescr() {
		return this.reportDescr;
	}

	public void setReportDescr(String reportDescr) {
		this.reportDescr = reportDescr;
	}

	public String getReportName() {
		return this.reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportView() {
		return this.reportView;
	}

	public void setReportView(String reportView) {
		this.reportView = reportView;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public List<TblReportFilter> getTblReportFilters() {
		return this.tblReportFilters;
	}

	public void setTblReportFilters(List<TblReportFilter> tblReportFilters) {
		this.tblReportFilters = tblReportFilters;
	}

	public List<TblReportQuery> getTblReportQueries() {
		return this.tblReportQueries;
	}

	public void setTblReportQueries(List<TblReportQuery> tblReportQueries) {
		this.tblReportQueries = tblReportQueries;
	}

}