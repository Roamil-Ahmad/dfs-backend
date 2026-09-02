package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the LKP_ISSUE_TYPE database table.
 * 
 */
@Entity
@Table(name="LKP_ISSUE_TYPE")
@NamedQuery(name="LkpIssueType.findAll", query="SELECT l FROM LkpIssueType l")
public class LkpIssueType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_ISSUE_TYPE_ISSUETYPEID_GENERATOR", sequenceName="LKP_ISSUE_TYPE_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_ISSUE_TYPE_ISSUETYPEID_GENERATOR")
	@Column(name="ISSUE_TYPE_ID")
	private long issueTypeId;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	@Column(name="ISSUE_TYPE_CODE")
	private String issueTypeCode;

	@Column(name="ISSUE_TYPE_DESCR")
	private String issueTypeDescr;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	public LkpIssueType() {
	}

	public long getIssueTypeId() {
		return this.issueTypeId;
	}

	public void setIssueTypeId(long issueTypeId) {
		this.issueTypeId = issueTypeId;
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

	public String getIssueTypeCode() {
		return this.issueTypeCode;
	}

	public void setIssueTypeCode(String issueTypeCode) {
		this.issueTypeCode = issueTypeCode;
	}

	public String getIssueTypeDescr() {
		return this.issueTypeDescr;
	}

	public void setIssueTypeDescr(String issueTypeDescr) {
		this.issueTypeDescr = issueTypeDescr;
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

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}
}