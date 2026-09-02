package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;


/**
 * The persistent class for the LKP_DISPUTE_TYPE database table.
 * 
 */
@Entity
@Table(name="LKP_DISPUTE_TYPE")
@NamedQuery(name="LkpDisputeType.findAll", query="SELECT l FROM LkpDisputeType l")
public class LkpDisputeType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_DISPUTE_TYPE_DISPUTETYPEID_GENERATOR", sequenceName="LKP_DISPUTE_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_DISPUTE_TYPE_DISPUTETYPEID_GENERATOR")
	@Column(name="DISPUTE_TYPE_ID")
	private long disputeTypeId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DISPUTE_TYPE_CODE")
	private String disputeTypeCode;

	@Column(name="DISPUTE_TYPE_DESCR")
	private String disputeTypeDescr;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	public LkpDisputeType() {
	}

	public long getDisputeTypeId() {
		return this.disputeTypeId;
	}

	public void setDisputeTypeId(long disputeTypeId) {
		this.disputeTypeId = disputeTypeId;
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

	public String getDisputeTypeCode() {
		return this.disputeTypeCode;
	}

	public void setDisputeTypeCode(String disputeTypeCode) {
		this.disputeTypeCode = disputeTypeCode;
	}

	public String getDisputeTypeDescr() {
		return this.disputeTypeDescr;
	}

	public void setDisputeTypeDescr(String disputeTypeDescr) {
		this.disputeTypeDescr = disputeTypeDescr;
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

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

}