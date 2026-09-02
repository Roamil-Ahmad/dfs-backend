package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
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
	@SequenceGenerator(name="TBL_MC_NOTIFICATION_MCNOTIFICATIONID_GENERATOR", sequenceName="TBL_MC_NOTIFICATION_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MC_NOTIFICATION_MCNOTIFICATIONID_GENERATOR")
	@Column(name="MC_NOTIFICATION_ID")
	private long mcNotificationId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblMcPendingRequest
	@ManyToOne
	@JoinColumn(name="MC_PENDING_REQUEST_ID")
	private TblMcPendingRequest tblMcPendingRequest;

	//bi-directional many-to-one association to TblUser
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TblUser tblUser;

	public TblMcNotification() {
	}

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

	public TblMcPendingRequest getTblMcPendingRequest() {
		return this.tblMcPendingRequest;
	}

	public void setTblMcPendingRequest(TblMcPendingRequest tblMcPendingRequest) {
		this.tblMcPendingRequest = tblMcPendingRequest;
	}

	public TblUser getTblUser() {
		return this.tblUser;
	}

	public void setTblUser(TblUser tblUser) {
		this.tblUser = tblUser;
	}

}