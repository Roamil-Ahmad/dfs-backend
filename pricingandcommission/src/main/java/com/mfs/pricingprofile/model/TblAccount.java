package com.mfs.pricingprofile.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_ACCOUNT database table.
 * 
 */
@Entity
@Table(name="TBL_ACCOUNT")
@NamedQuery(name="TblAccount.findAll", query="SELECT t FROM TblAccount t")
public class TblAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_ACCOUNT_ACCOUNTID_GENERATOR", sequenceName="TBL_ACCOUNT_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_ACCOUNT_ACCOUNTID_GENERATOR")
	@Column(name="ACCOUNT_ID")
	private long accountId;

	@Column(name="ACCOUNT_NO")
	private String accountNo;

	@Column(name="ACCOUNT_TITLE")
	private String accountTitle;

	@Column(name="AGENT_ACCOUNT")
	private String agentAccount;

	@Column(name="APPROVED_YN")
	private String approvedYn;

	private String comments;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Column(name="CURRENT_BALANCE")
	private BigDecimal currentBalance;

	private String iban;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="LKP_ACCOUNT_STATUS_ID")
	private BigDecimal lkpAccountStatusId;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblAccountLevel
	@ManyToOne
	@JoinColumn(name="ACCOUNT_LEVEL_ID")
	private TblAccountLevel tblAccountLevel;

	//bi-directional many-to-one association to TblAgent
	@ManyToOne
	@JoinColumn(name="AGENT_ID")
	private TblAgent tblAgent;

	//bi-directional many-to-one association to TblCustomer
	@ManyToOne
	@JoinColumn(name="CUSTOMER_ID")
	private TblCustomer tblCustomer;

	public long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountTitle() {
		return this.accountTitle;
	}

	public void setAccountTitle(String accountTitle) {
		this.accountTitle = accountTitle;
	}

	public String getAgentAccount() {
		return this.agentAccount;
	}

	public void setAgentAccount(String agentAccount) {
		this.agentAccount = agentAccount;
	}

	public String getApprovedYn() {
		return this.approvedYn;
	}

	public void setApprovedYn(String approvedYn) {
		this.approvedYn = approvedYn;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public BigDecimal getCurrentBalance() {
		return this.currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
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

	public BigDecimal getLkpAccountStatusId() {
		return this.lkpAccountStatusId;
	}

	public void setLkpAccountStatusId(BigDecimal lkpAccountStatusId) {
		this.lkpAccountStatusId = lkpAccountStatusId;
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

	public TblAccountLevel getTblAccountLevel() {
		return this.tblAccountLevel;
	}

	public void setTblAccountLevel(TblAccountLevel tblAccountLevel) {
		this.tblAccountLevel = tblAccountLevel;
	}

	public TblAgent getTblAgent() {
		return this.tblAgent;
	}

	public void setTblAgent(TblAgent tblAgent) {
		this.tblAgent = tblAgent;
	}

	public TblCustomer getTblCustomer() {
		return this.tblCustomer;
	}

	public void setTblCustomer(TblCustomer tblCustomer) {
		this.tblCustomer = tblCustomer;
	}

}