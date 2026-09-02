package com.workflow.modal;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_MC_NOTIFICATION database table.
 * 
 */
@Entity
@Table(name="TBL_MC_NOTIFICATION")
@NamedQuery(name="TblMcNotification.findAll", query="SELECT t FROM TblMcNotification t")
public class TblMcNotification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_MC_NOTIFICATION_GENERATOR", sequenceName="TBL_MC_NOTIFICATION_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MC_NOTIFICATION_GENERATOR")
	@Column(name="MC_NOTIFICATION_ID")
	private long mcNotificationId;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;


	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MC_PENDING_REQUEST_ID")
	private BigDecimal mcPendingRequestId;

	private BigDecimal updateindex;

	@Column(name="USER_ID")
	private BigDecimal userId;


	public long getMcNotificationId() {
		return this.mcNotificationId;
	}

	public void setMcNotificationId(long mcNotificationId) {
		this.mcNotificationId = mcNotificationId;
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

	public BigDecimal getMcPendingRequestId() {
		return mcPendingRequestId;
	}

	public void setMcPendingRequestId(BigDecimal mcPendingRequestId) {
		this.mcPendingRequestId = mcPendingRequestId;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public BigDecimal getUserId() {
		return this.userId;
	}

	public void setUserId(BigDecimal userId) {
		this.userId = userId;
	}

}