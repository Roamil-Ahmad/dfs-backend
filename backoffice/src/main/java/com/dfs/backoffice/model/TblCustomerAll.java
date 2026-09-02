package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the TBL_CUSTOMER_ALL database table.
 * 
 */
@Entity
@Table(name="TBL_CUSTOMER_ALL")
@NamedQuery(name="TblCustomerAll.findAll", query="SELECT t FROM TblCustomerAll t")
public class TblCustomerAll implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_CUSTOMER_ALL_CUSTOMERALLID_GENERATOR", sequenceName="TBL_CUSTOMER_ALL_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CUSTOMER_ALL_CUSTOMERALLID_GENERATOR")
	@Column(name="CUSTOMER_ALL_ID")
	private long customerAllId;

	@Column(name="APP_VERSION")
	private String appVersion;

	private String bioverified;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="DEVICE_MODEL")
	private String deviceModel;

	@Column(name="IMEI_NO")
	private String imeiNo;

	@Column(name="IP_ADDRESS_A")
	private String ipAddressA;

	@Column(name="IP_ADDRESS_P")
	private String ipAddressP;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	private double latitude;

	private double longitude;

	@Column(name="MAC_ADDRESS")
	private String macAddress;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	private String otpverified;

	private String selfieverified;

	private BigDecimal updateindex;

	private String uuid;

	//bi-directional many-to-one association to LkpChannel
	@ManyToOne
	@JoinColumn(name="CHANNEL_ID")
	private LkpChannel lkpChannel;

	//bi-directional many-to-one association to LkpNetwork
	@ManyToOne
	@JoinColumn(name="NETWORK_ID")
	private LkpNetwork lkpNetwork;


	public long getCustomerAllId() {
		return this.customerAllId;
	}

	public void setCustomerAllId(long customerAllId) {
		this.customerAllId = customerAllId;
	}

	public String getAppVersion() {
		return this.appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getBioverified() {
		return this.bioverified;
	}

	public void setBioverified(String bioverified) {
		this.bioverified = bioverified;
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

	public String getIpAddressA() {
		return this.ipAddressA;
	}

	public void setIpAddressA(String ipAddressA) {
		this.ipAddressA = ipAddressA;
	}

	public String getIpAddressP() {
		return this.ipAddressP;
	}

	public void setIpAddressP(String ipAddressP) {
		this.ipAddressP = ipAddressP;
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

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
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

	public String getOtpverified() {
		return this.otpverified;
	}

	public void setOtpverified(String otpverified) {
		this.otpverified = otpverified;
	}

	public String getSelfieverified() {
		return this.selfieverified;
	}

	public void setSelfieverified(String selfieverified) {
		this.selfieverified = selfieverified;
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

	public LkpChannel getLkpChannel() {
		return this.lkpChannel;
	}

	public void setLkpChannel(LkpChannel lkpChannel) {
		this.lkpChannel = lkpChannel;
	}

	public LkpNetwork getLkpNetwork() {
		return this.lkpNetwork;
	}

	public void setLkpNetwork(LkpNetwork lkpNetwork) {
		this.lkpNetwork = lkpNetwork;
	}

}