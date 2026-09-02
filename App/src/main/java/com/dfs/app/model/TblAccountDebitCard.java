package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_ACCOUNT_DEBIT_CARD database table.
 * 
 */
@Entity
@Table(name="TBL_ACCOUNT_DEBIT_CARD")
@NamedQuery(name="TblAccountDebitCard.findAll", query="SELECT t FROM TblAccountDebitCard t")
public class TblAccountDebitCard implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_ACCOUNT_DEBIT_CARD_ACCOUNTDEBITCARDID_GENERATOR", sequenceName="TBL_ACCOUNT_DEBIT_CARD_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_ACCOUNT_DEBIT_CARD_ACCOUNTDEBITCARDID_GENERATOR")
	@Column(name="ACCOUNT_DEBIT_CARD_ID")
	private long accountDebitCardId;

	@Column(name="ACCOUNT_NO")
	private String accountNo;

	@Column(name="CARD_ID")
	private String cardId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="EXPIRY_DATE")
	private String expiryDate;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private String pan;

	@Column(name="RENEWAL_CHARGES_COUNT")
	private BigDecimal renewalChargesCount;

	private String status;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpBranch
	@ManyToOne
	@JoinColumn(name="BRANCH_ID")
	private LkpBranch lkpBranch;

	//bi-directional many-to-one association to TblAccount
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TblAccount tblAccount;

	public TblAccountDebitCard() {
	}

	public long getAccountDebitCardId() {
		return this.accountDebitCardId;
	}

	public void setAccountDebitCardId(long accountDebitCardId) {
		this.accountDebitCardId = accountDebitCardId;
	}

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCardId() {
		return this.cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
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

	public String getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
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

	public String getPan() {
		return this.pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public BigDecimal getRenewalChargesCount() {
		return this.renewalChargesCount;
	}

	public void setRenewalChargesCount(BigDecimal renewalChargesCount) {
		this.renewalChargesCount = renewalChargesCount;
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

	public LkpBranch getLkpBranch() {
		return this.lkpBranch;
	}

	public void setLkpBranch(LkpBranch lkpBranch) {
		this.lkpBranch = lkpBranch;
	}

	public TblAccount getTblAccount() {
		return this.tblAccount;
	}

	public void setTblAccount(TblAccount tblAccount) {
		this.tblAccount = tblAccount;
	}

}