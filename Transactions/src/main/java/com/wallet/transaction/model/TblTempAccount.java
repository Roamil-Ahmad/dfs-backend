package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_TEMP_ACCOUNT database table.
 * 
 */
@Entity
@Table(name="TBL_TEMP_ACCOUNT")
@NamedQuery(name="TblTempAccount.findAll", query="SELECT t FROM TblTempAccount t")
public class TblTempAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TEMP_ACCOUNT_TEMPACCOUNTID_GENERATOR", sequenceName="TBL_TEMP_ACCOUNT_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TEMP_ACCOUNT_TEMPACCOUNTID_GENERATOR")
	@Column(name="TEMP_ACCOUNT_ID")
	private long tempAccountId;
	@Column(name="NID_NO")

	private BigDecimal cnic;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	private String name;

	private String status;

	private BigDecimal updateindex;

	public TblTempAccount() {
	}

	public long getTempAccountId() {
		return this.tempAccountId;
	}

	public void setTempAccountId(long tempAccountId) {
		this.tempAccountId = tempAccountId;
	}

	public BigDecimal getCnic() {
		return this.cnic;
	}

	public void setCnic(BigDecimal cnic) {
		this.cnic = cnic;
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

}