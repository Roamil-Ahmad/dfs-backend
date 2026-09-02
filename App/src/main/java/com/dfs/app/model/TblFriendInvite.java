package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_FRIEND_INVITE database table.
 * 
 */
@Entity
@Table(name="TBL_FRIEND_INVITE")
@NamedQuery(name="TblFriendInvite.findAll", query="SELECT t FROM TblFriendInvite t")
public class TblFriendInvite implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_FRIEND_INVITE_FRIENDINVITEID_GENERATOR", sequenceName="TBL_FRIEND_INVITE_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_FRIEND_INVITE_FRIENDINVITEID_GENERATOR")
	@Column(name="FRIEND_INVITE_ID")
	private long friendInviteId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="INVITEE_ACCOUNT_ID")
	private BigDecimal inviteeAccountId;

	@Column(name="INVITOR_ACCOUNT_ID")
	private BigDecimal invitorAccountId;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	private String name;

	private String status;

	private BigDecimal updateindex;

	public TblFriendInvite() {
	}

	public long getFriendInviteId() {
		return this.friendInviteId;
	}

	public void setFriendInviteId(long friendInviteId) {
		this.friendInviteId = friendInviteId;
	}



	public BigDecimal getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(BigDecimal createuser) {
		this.createuser = createuser;
	}

	public BigDecimal getInviteeAccountId() {
		return this.inviteeAccountId;
	}

	public void setInviteeAccountId(BigDecimal inviteeAccountId) {
		this.inviteeAccountId = inviteeAccountId;
	}

	public BigDecimal getInvitorAccountId() {
		return this.invitorAccountId;
	}

	public void setInvitorAccountId(BigDecimal invitorAccountId) {
		this.invitorAccountId = invitorAccountId;
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

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
}