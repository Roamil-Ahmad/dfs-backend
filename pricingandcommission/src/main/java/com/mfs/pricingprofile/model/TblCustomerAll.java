package com.mfs.pricingprofile.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
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

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DEVICE_MODEL")
	private String deviceModel;

	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_FROM")
	private Date effectiveFrom;

	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_TO")
	private Date effectiveTo;

	@Column(name="IMEI_NO")
	private String imeiNo;

	@Column(name="IP_ADDRESS_A")
	private String ipAddressA;

	@Column(name="IP_ADDRESS_P")
	private String ipAddressP;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private double latitude;

	@Column(name="LKP_NETWORK_ID")
	private BigDecimal lkpNetworkId;

	private double longitude;

	@Column(name="MAC_ADDRESS")
	private String macAddress;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	@Column(name="OTP_TRIES")
	private BigDecimal otpTries;

	private String otpin;

	@Column(name="TIME_ZONE")
	private String timeZone;

	private String tmsverified;

	private BigDecimal updateindex;

	private String verified;

	//bi-directional many-to-one association to TblCustomer
	@OneToMany(mappedBy="tblCustomerAll")
	private List<TblCustomer> tblCustomers;

	//bi-directional many-to-one association to LkpChannel
	@ManyToOne
	@JoinColumn(name="LKP_CHANNEL_ID")
	private LkpChannel lkpChannel;

	public TblCustomerAll() {
	}

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

	public Date getEffectiveFrom() {
		return this.effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Date getEffectiveTo() {
		return this.effectiveTo;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
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

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLkpNetworkId() {
		return this.lkpNetworkId;
	}

	public void setLkpNetworkId(BigDecimal lkpNetworkId) {
		this.lkpNetworkId = lkpNetworkId;
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

	public BigDecimal getOtpTries() {
		return this.otpTries;
	}

	public void setOtpTries(BigDecimal otpTries) {
		this.otpTries = otpTries;
	}

	public String getOtpin() {
		return this.otpin;
	}

	public void setOtpin(String otpin) {
		this.otpin = otpin;
	}

	public String getTimeZone() {
		return this.timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getTmsverified() {
		return this.tmsverified;
	}

	public void setTmsverified(String tmsverified) {
		this.tmsverified = tmsverified;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public String getVerified() {
		return this.verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public List<TblCustomer> getTblCustomers() {
		return this.tblCustomers;
	}

	public void setTblCustomers(List<TblCustomer> tblCustomers) {
		this.tblCustomers = tblCustomers;
	}

	public TblCustomer addTblCustomer(TblCustomer tblCustomer) {
		getTblCustomers().add(tblCustomer);
		tblCustomer.setTblCustomerAll(this);

		return tblCustomer;
	}

	public TblCustomer removeTblCustomer(TblCustomer tblCustomer) {
		getTblCustomers().remove(tblCustomer);
		tblCustomer.setTblCustomerAll(null);

		return tblCustomer;
	}

	public LkpChannel getLkpChannel() {
		return this.lkpChannel;
	}

	public void setLkpChannel(LkpChannel lkpChannel) {
		this.lkpChannel = lkpChannel;
	}

}