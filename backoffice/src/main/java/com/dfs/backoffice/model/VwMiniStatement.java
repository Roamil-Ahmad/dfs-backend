package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the VW_MINI_STATEMENT database table.
 * 
 */
@Entity
@Table(name="VW_MINI_STATEMENT")
@NamedQuery(name="VwMiniStatement.findAll", query="SELECT v FROM VwMiniStatement v")
public class VwMiniStatement implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal amount;

	@Column(name="AMOUNT_TYPE")
	private String amountType;

	@Column(name="AMOUNT_TYPE2")
	private String amountType2;

	private String channel;

	@Column(name="CHARGES_ID")
	private BigDecimal chargesId;

	@Column(name="CL_BALANCE")
	private BigDecimal clBalance;

	private String comments;

	@Column(name="DESTINATION_BANK")
	private String destinationBank;

	@Column(name="FEE_AMOUNT")
	private BigDecimal feeAmount;

	@Column(name="FROM_ACCOUNT_ID")
	private BigDecimal fromAccountId;

	@Column(name="FROM_ACCOUNT_NO")
	private String fromAccountNo;

	@Column(name="FROM_ACCOUNT_TITLE")
	private String fromAccountTitle;

	@Column(name="FROM_ACCOUNT_TYPE")
	private String fromAccountType;

	@Column(name="OP_BALANCE")
	private BigDecimal opBalance;

	private String rrn;

	@Column(name="SOURCE_BANK")
	private String sourceBank;

	private String stan;

	@Id
	@Column(name="TO_ACCOUNT_ID")
	private BigDecimal toAccountId;

	@Column(name="TO_ACCOUNT_NO")
	private String toAccountNo;

	@Column(name="TO_ACCOUNT_TITLE")
	private String toAccountTitle;

	@Column(name="TO_ACCOUNT_TYPE")
	private String toAccountType;

	@Column(name="TRANS_DATE")
	private Timestamp transDate;

	@Column(name="TRANS_DOCS_CODE")
	private String transDocsCode;

	@Column(name="TRANS_DOCS_DESCR")
	private String transDocsDescr;

	@Column(name="TRANS_REFNUM")
	private BigDecimal transRefnum;

	@Column(name="VALUE_DATE")
	private String valueDate;

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getAmountType() {
		return this.amountType;
	}

	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}

	public String getAmountType2() {
		return this.amountType2;
	}

	public void setAmountType2(String amountType2) {
		this.amountType2 = amountType2;
	}

	public String getChannel() {
		return this.channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public BigDecimal getChargesId() {
		return this.chargesId;
	}

	public void setChargesId(BigDecimal chargesId) {
		this.chargesId = chargesId;
	}

	public BigDecimal getClBalance() {
		return this.clBalance;
	}

	public void setClBalance(BigDecimal clBalance) {
		this.clBalance = clBalance;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDestinationBank() {
		return this.destinationBank;
	}

	public void setDestinationBank(String destinationBank) {
		this.destinationBank = destinationBank;
	}

	public BigDecimal getFeeAmount() {
		return this.feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public BigDecimal getFromAccountId() {
		return this.fromAccountId;
	}

	public void setFromAccountId(BigDecimal fromAccountId) {
		this.fromAccountId = fromAccountId;
	}

	public String getFromAccountNo() {
		return this.fromAccountNo;
	}

	public void setFromAccountNo(String fromAccountNo) {
		this.fromAccountNo = fromAccountNo;
	}

	public String getFromAccountTitle() {
		return this.fromAccountTitle;
	}

	public void setFromAccountTitle(String fromAccountTitle) {
		this.fromAccountTitle = fromAccountTitle;
	}

	public String getFromAccountType() {
		return this.fromAccountType;
	}

	public void setFromAccountType(String fromAccountType) {
		this.fromAccountType = fromAccountType;
	}

	public BigDecimal getOpBalance() {
		return this.opBalance;
	}

	public void setOpBalance(BigDecimal opBalance) {
		this.opBalance = opBalance;
	}

	public String getRrn() {
		return this.rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getSourceBank() {
		return this.sourceBank;
	}

	public void setSourceBank(String sourceBank) {
		this.sourceBank = sourceBank;
	}

	public String getStan() {
		return this.stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	public BigDecimal getToAccountId() {
		return this.toAccountId;
	}

	public void setToAccountId(BigDecimal toAccountId) {
		this.toAccountId = toAccountId;
	}

	public String getToAccountNo() {
		return this.toAccountNo;
	}

	public void setToAccountNo(String toAccountNo) {
		this.toAccountNo = toAccountNo;
	}

	public String getToAccountTitle() {
		return this.toAccountTitle;
	}

	public void setToAccountTitle(String toAccountTitle) {
		this.toAccountTitle = toAccountTitle;
	}

	public String getToAccountType() {
		return this.toAccountType;
	}

	public void setToAccountType(String toAccountType) {
		this.toAccountType = toAccountType;
	}

	public Timestamp getTransDate() {
		return this.transDate;
	}

	public void setTransDate(Timestamp transDate) {
		this.transDate = transDate;
	}

	public String getTransDocsCode() {
		return this.transDocsCode;
	}

	public void setTransDocsCode(String transDocsCode) {
		this.transDocsCode = transDocsCode;
	}

	public String getTransDocsDescr() {
		return this.transDocsDescr;
	}

	public void setTransDocsDescr(String transDocsDescr) {
		this.transDocsDescr = transDocsDescr;
	}

	public BigDecimal getTransRefnum() {
		return this.transRefnum;
	}

	public void setTransRefnum(BigDecimal transRefnum) {
		this.transRefnum = transRefnum;
	}

	public String getValueDate() {
		return this.valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

}