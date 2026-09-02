package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_DEVICE_INFO database table.
 * 
 */
@Entity
@Table(name="TBL_DEVICE_INFO")
@NamedQuery(name="TblDeviceInfo.findAll", query="SELECT t FROM TblDeviceInfo t")
public class TblDeviceInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_DEVICE_INFO_DEVICEINFOID_GENERATOR", sequenceName="TBL_DEVICE_INFO_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_DEVICE_INFO_DEVICEINFOID_GENERATOR")
	@Column(name="DEVICE_INFO_ID")
	private long deviceInfoId;

	@Column(name="APP_VERSION")
	private String appVersion;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DEVICE_MODEL")
	private String deviceModel;

	@Column(name="IMEI_NO")
	private String imeiNo;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MAC_ADDRESS")
	private String macAddress;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	private BigDecimal updateindex;

	@Column(name="UUID")
	private String uuid;

	//bi-directional many-to-one association to TblAppUser
	@ManyToOne
	@JoinColumn(name="APP_USER_ID")
	private TblAppUser tblAppUser;

	//bi-directional many-to-one association to TblCustomerAll
	@ManyToOne
	@JoinColumn(name="CUSTOMER_ALL_ID")
	private TblCustomerAll tblCustomerAll;

	public TblDeviceInfo() {
		this.tblAppUser=new TblAppUser();
	}

	public long getDeviceInfoId() {
		return this.deviceInfoId;
	}

	public void setDeviceInfoId(long deviceInfoId) {
		this.deviceInfoId = deviceInfoId;
	}

	public String getAppVersion() {
		return this.appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
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

	public String getDeviceModel() {
		return this.deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getImeiNo() {
		return this.imeiNo;
	}

	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
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

	public String getMacAddress() {
		return this.macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public TblAppUser getTblAppUser() {
		return this.tblAppUser;
	}

	public void setTblAppUser(TblAppUser tblAppUser) {
		this.tblAppUser = tblAppUser;
	}

	public TblCustomerAll getTblCustomerAll() {
		return this.tblCustomerAll;
	}

	public void setTblCustomerAll(TblCustomerAll tblCustomerAll) {
		this.tblCustomerAll = tblCustomerAll;
	}

}