package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_NOTIFICATION database table.
 * 
 */
@Entity
@Table(name="TBL_NOTIFICATION")
@NamedQuery(name="TblNotification.findAll", query="SELECT t FROM TblNotification t")
public class TblNotification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_NOTIFICATION_NOTIFICATIONID_GENERATOR", sequenceName="TBL_NOTIFICATION_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_NOTIFICATION_NOTIFICATIONID_GENERATOR")
	@Column(name="NOTIFICATION_ID")
	private long notificationId;

	@Column(name="APP_USER_ID")
	private BigDecimal appUserId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="NOTIFICATION_MESSAGE")
	private String notificationMessage;

	@Column(name="NOTIFICATION_TITLE")
	private String notificationTitle;

	@Column(name="NOTIFICATION_TYPE")
	private String notificationType;

	private BigDecimal updateindex;

	public TblNotification() {
	}

	public long getNotificationId() {
		return this.notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	public BigDecimal getAppUserId() {
		return this.appUserId;
	}

	public void setAppUserId(BigDecimal appUserId) {
		this.appUserId = appUserId;
	}

	public Date getCreatedate() {
		return createdate;
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
		return lastupdatedate;
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

	public String getNotificationMessage() {
		return this.notificationMessage;
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}

	public String getNotificationTitle() {
		return this.notificationTitle;
	}

	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}

	public String getNotificationType() {
		return this.notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}