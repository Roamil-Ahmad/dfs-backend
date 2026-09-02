package com.dfs.backoffice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_REPORT_QUERY database table.
 * 
 */
@Entity
@Table(name="TBL_REPORT_QUERY")
@NamedQuery(name="TblReportQuery.findAll", query="SELECT t FROM TblReportQuery t")
public class TblReportQuery implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_REPORT_QUERY_REPORTQUERYID_GENERATOR", sequenceName="TBL_REPORT_QUERY_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_REPORT_QUERY_REPORTQUERYID_GENERATOR")
	@Column(name="REPORT_QUERY_ID")
	private long reportQueryId;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	@Lob
	@Column(name="REPORT_COUNT")
	private String reportCount;

	@Column(name="REPORT_QUERY_NAME")
	private String reportQueryName;

	@Lob
	@Column(name="REPORT_QUERY_SAMPLE")
	private String reportQuerySample;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblReport
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="REPORT_ID")
	private TblReport tblReport;

	public long getReportQueryId() {
		return this.reportQueryId;
	}

	public void setReportQueryId(long reportQueryId) {
		this.reportQueryId = reportQueryId;
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

	public String getReportCount() {
		return this.reportCount;
	}

	public void setReportCount(String reportCount) {
		this.reportCount = reportCount;
	}

	public String getReportQueryName() {
		return this.reportQueryName;
	}

	public void setReportQueryName(String reportQueryName) {
		this.reportQueryName = reportQueryName;
	}

	public String getReportQuerySample() {
		return this.reportQuerySample;
	}

	public void setReportQuerySample(String reportQuerySample) {
		this.reportQuerySample = reportQuerySample;
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
}