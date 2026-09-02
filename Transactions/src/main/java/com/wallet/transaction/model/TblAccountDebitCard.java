package com.wallet.transaction.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


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
	@SequenceGenerator(name="TBL_ACCOUNT_DEBIT_CARD_ACCOUNTDEBITCARDID_GENERATOR", sequenceName="TBL_ACCOUNT_DEBIT_CARD_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_ACCOUNT_DEBIT_CARD_ACCOUNTDEBITCARDID_GENERATOR")
	@Column(name="ACCOUNT_DEBIT_CARD_ID")
	private long accountDebitCardId;

	@Column(name="ACCOUNT_NO")
	private String accountNo;

	//bi-directional many-to-one association to LkpBranch
	@ManyToOne
	@JoinColumn(name="BANK_ID")
	private LkpBank lkpBank;

	//bi-directional many-to-one association to LkpBranch
	@ManyToOne
	@JoinColumn(name="BRANCH_ID")
	private LkpBranch lkpBranch;

	@Column(name="CARD_ID")
	private String cardId;

	private Date createdate;

	private BigDecimal createuser;

	//bi-directional many-to-one association to LkpBranch
	@ManyToOne
	@JoinColumn(name="CURRENCY_ID")
	private LkpCurrency lkpCurrency;

	private String cvv;

	@Column(name="EXPIRY_DATE")
	private String expiryDate;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="LINKED_CARD")
	private String linkedCard;

	@Column(name="NAME_ON_CARD")
	private String nameOnCard;

	private String pan;

	@Column(name="RENEWAL_CHARGES_COUNT")
	private BigDecimal renewalChargesCount;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblAccount
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TblAccount tblAccount;

	@Column(name="IS_DEFAULT")
	private String isDefault;

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

	public LkpBank getLkpBank() {
		return lkpBank;
	}

	public void setLkpBank(LkpBank lkpBank) {
		this.lkpBank = lkpBank;
	}

	public LkpBranch getLkpBranch() {
		return lkpBranch;
	}

	public void setLkpBranch(LkpBranch lkpBranch) {
		this.lkpBranch = lkpBranch;
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

	public LkpCurrency getLkpCurrency() {
		return lkpCurrency;
	}

	public void setLkpCurrency(LkpCurrency lkpCurrency) {
		this.lkpCurrency = lkpCurrency;
	}

	public String getCvv() {
		return this.cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
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

	public String getLinkedCard() {
		return this.linkedCard;
	}

	public void setLinkedCard(String linkedCard) {
		this.linkedCard = linkedCard;
	}

	public String getNameOnCard() {
		return this.nameOnCard;
	}

	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
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

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public TblAccount getTblAccount() {
		return this.tblAccount;
	}

	public void setTblAccount(TblAccount tblAccount) {
		this.tblAccount = tblAccount;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
}