package com.workflow.modal;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_ALERT_DETAIL database table.
 * 
 */
@Entity
@Table(name="TBL_ALERT_DETAIL")
@NamedQuery(name="TblAlertDetail.findAll", query="SELECT t FROM TblAlertDetail t")
public class TblAlertDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_ALERT_DETAIL_ALERTDETAILID_GENERATOR", sequenceName="TBL_ALERT_DETAIL_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_ALERT_DETAIL_ALERTDETAILID_GENERATOR")
	@Column(name="ALERT_DETAIL_ID")
	private long alertDetailId;

	@Column(name="ALERT_CONFIG_ID")
	private BigDecimal alertConfigId;

	@Column(name="CREATED_BY")
	private BigDecimal createdBy;

	@Column(name="CREATED_ON")
	private Timestamp createdOn;

	@Column(name="DURATION_HOURS")
	private BigDecimal durationHours;

	@Column(name="EMAIL_SUBJECT")
	private String emailSubject;

	private String receipents;

	@Column(name="RECORD_COUNT")
	private BigDecimal recordCount;

	@Lob
	@Column(name="REPORT_DATA")
	private byte[] reportData;

	private String status;

	@Column(name="UPDATED_BY")
	private BigDecimal updatedBy;

	@Column(name="UPDATED_ON")
	private Timestamp updatedOn;

	public long getAlertDetailId() {
		return this.alertDetailId;
	}

	public void setAlertDetailId(long alertDetailId) {
		this.alertDetailId = alertDetailId;
	}

	public BigDecimal getAlertConfigId() {
		return this.alertConfigId;
	}

	public void setAlertConfigId(BigDecimal alertConfigId) {
		this.alertConfigId = alertConfigId;
	}

	public BigDecimal getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(BigDecimal createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public BigDecimal getDurationHours() {
		return this.durationHours;
	}

	public void setDurationHours(BigDecimal durationHours) {
		this.durationHours = durationHours;
	}

	public String getEmailSubject() {
		return this.emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getReceipents() {
		return this.receipents;
	}

	public void setReceipents(String receipents) {
		this.receipents = receipents;
	}

	public BigDecimal getRecordCount() {
		return this.recordCount;
	}

	public void setRecordCount(BigDecimal recordCount) {
		this.recordCount = recordCount;
	}

	public byte[] getReportData() {
		return this.reportData;
	}

	public void setReportData(byte[] reportData) {
		this.reportData = reportData;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(BigDecimal updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}

}