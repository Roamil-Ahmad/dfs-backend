package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_BENEFICIARY database table.
 * 
 */
@Entity
@Table(name="TBL_BENEFICIARY")
@NamedQuery(name="TblBeneficiary.findAll", query="SELECT t FROM TblBeneficiary t")
public class TblBeneficiary implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_BENEFICIARY_BENEFICIARYID_GENERATOR", sequenceName="TBL_BENEFICIARY_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_BENEFICIARY_BENEFICIARYID_GENERATOR")
	@Column(name="BENEFICIARY_ID")
	private long beneficiaryId;

	@Column(name="BENEFICIARY_ACCOUNT_NO")
	private String beneficiaryAccountNo;

	@Column(name="BENEFICIARY_ACCOUNT_TITLE")
	private String beneficiaryAccountTitle;

	@Column(name="BENEFICIARY_ACCT_TYPE")
	private String beneficiaryAcctType;

	@Column(name="BENEFICIARY_EMAIL")
	private String beneficiaryEmail;

	@Column(name="BENEFICIARY_IMD")
	private String beneficiaryImd;

	@Column(name="BENEFICIARY_IMD_ID")
	private BigDecimal beneficiaryImdId;

	@Column(name="BENEFICIARY_MOBILE_NO")
	private String beneficiaryMobileNo;

	@Column(name="BENEFICIARY_NICK_NAME")
	private String beneficiaryNickName;

	@Column(name="BENEFICIARY_TYPE")
	private String beneficiaryType;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblCustomer
	@ManyToOne
	@JoinColumn(name="CUSTOMER_ID")
	private TblCustomer tblCustomer;

	@Column(name="ACCOUNT_DEBIT_CARD_ID")
	private BigDecimal accountDebitCardId;

	public TblBeneficiary() {
	}

	public long getBeneficiaryId() {
		return this.beneficiaryId;
	}

	public void setBeneficiaryId(long beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	public String getBeneficiaryAccountNo() {
		return this.beneficiaryAccountNo;
	}

	public void setBeneficiaryAccountNo(String beneficiaryAccountNo) {
		this.beneficiaryAccountNo = beneficiaryAccountNo;
	}

	public String getBeneficiaryAccountTitle() {
		return this.beneficiaryAccountTitle;
	}

	public void setBeneficiaryAccountTitle(String beneficiaryAccountTitle) {
		this.beneficiaryAccountTitle = beneficiaryAccountTitle;
	}

	public String getBeneficiaryAcctType() {
		return this.beneficiaryAcctType;
	}

	public void setBeneficiaryAcctType(String beneficiaryAcctType) {
		this.beneficiaryAcctType = beneficiaryAcctType;
	}

	public String getBeneficiaryEmail() {
		return this.beneficiaryEmail;
	}

	public void setBeneficiaryEmail(String beneficiaryEmail) {
		this.beneficiaryEmail = beneficiaryEmail;
	}

	public String getBeneficiaryImd() {
		return this.beneficiaryImd;
	}

	public void setBeneficiaryImd(String beneficiaryImd) {
		this.beneficiaryImd = beneficiaryImd;
	}

	public BigDecimal getBeneficiaryImdId() {
		return this.beneficiaryImdId;
	}

	public void setBeneficiaryImdId(BigDecimal beneficiaryImdId) {
		this.beneficiaryImdId = beneficiaryImdId;
	}

	public String getBeneficiaryMobileNo() {
		return this.beneficiaryMobileNo;
	}

	public void setBeneficiaryMobileNo(String beneficiaryMobileNo) {
		this.beneficiaryMobileNo = beneficiaryMobileNo;
	}

	public String getBeneficiaryNickName() {
		return this.beneficiaryNickName;
	}

	public void setBeneficiaryNickName(String beneficiaryNickName) {
		this.beneficiaryNickName = beneficiaryNickName;
	}

	public String getBeneficiaryType() {
		return this.beneficiaryType;
	}

	public void setBeneficiaryType(String beneficiaryType) {
		this.beneficiaryType = beneficiaryType;
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

	public TblCustomer getTblCustomer() {
		return this.tblCustomer;
	}

	public void setTblCustomer(TblCustomer tblCustomer) {
		this.tblCustomer = tblCustomer;
	}

	public BigDecimal getAccountDebitCardId() {
		return accountDebitCardId;
	}

	public void setAccountDebitCardId(BigDecimal accountDebitCardId) {
		this.accountDebitCardId = accountDebitCardId;
	}
}