package com.dfs.app.model;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the TBL_BULK_ACCOUNTS database table.
 *
 * <p>Accounts submitted in bulk by the Corporate Portal, held here to be picked up and processed.
 * Nothing in this service reads the table back.</p>
 */
@Entity
@Table(name = "TBL_BULK_ACCOUNTS")
@NamedQuery(name = "TblBulkAccount.findAll", query = "SELECT t FROM TblBulkAccount t")
public class TblBulkAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "TBL_BULK_ACCOUNTS_BULKACCOUNTID_GENERATOR", sequenceName = "TBL_BULK_ACCOUNTS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_BULK_ACCOUNTS_BULKACCOUNTID_GENERATOR")
	@Column(name = "BULK_ACCOUNT_ID")
	private long bulkAccountId;

	@Column(name = "MOBILE_NO")
	private String mobileNo;

	@Column(name = "ACCOUNT_TITLE")
	private String accountTitle;

	@Column(name = "NID_NO")
	private String nidNo;

	@Column(name = "SEGMENT_DESCR")
	private String segmentDescr;

	private BigDecimal createuser;

	private Date createdate;

	private BigDecimal lastupdateuser;

	private Date lastupdatedate;

	private BigDecimal updateindex;

	public TblBulkAccount() {
	}

	public long getBulkAccountId() {
		return this.bulkAccountId;
	}

	public void setBulkAccountId(long bulkAccountId) {
		this.bulkAccountId = bulkAccountId;
	}

	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getAccountTitle() {
		return this.accountTitle;
	}

	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}

	public String getNidNo() {
		return this.nidNo;
	}

	public void setNidNo(String nidNo) {
		this.nidNo = nidNo;
	}

	public String getSegmentDescr() {
		return this.segmentDescr;
	}

	public void setSegmentDescr(String segmentDescr) {
		this.segmentDescr = segmentDescr;
	}

	public BigDecimal getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(BigDecimal createuser) {
		this.createuser = createuser;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public BigDecimal getLastupdateuser() {
		return this.lastupdateuser;
	}

	public void setLastupdateuser(BigDecimal lastupdateuser) {
		this.lastupdateuser = lastupdateuser;
	}

	public Date getLastupdatedate() {
		return this.lastupdatedate;
	}

	public void setLastupdatedate(Date lastupdatedate) {
		this.lastupdatedate = lastupdatedate;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}
}
