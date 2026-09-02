package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_DEBIT_CARD_RESPONSE database table.
 * 
 */
@Entity
@Table(name="TBL_DEBIT_CARD_RESPONSE")
@NamedQuery(name="TblDebitCardResponse.findAll", query="SELECT t FROM TblDebitCardResponse t")
public class TblDebitCardResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_DEBIT_CARD_RESPONSE_DEBITCARDRESPONSEID_GENERATOR", sequenceName="TBL_DEBIT_CARD_RESPONSE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_DEBIT_CARD_RESPONSE_DEBITCARDRESPONSEID_GENERATOR")
	@Column(name="DEBIT_CARD_RESPONSE_ID")
	private long debitCardResponseId;

	private String address1;

	private String address2;

	@Column(name="CARD_CATEGORY")
	private String cardCategory;

	@Column(name="CARD_ID")
	private String cardId;

	@Column(name="CARD_NUMBER")
	private String cardNumber;

	@Column(name="CARD_PRODUCT")
	private String cardProduct;

	@Column(name="CARD_PRODUCT_CODE")
	private String cardProductCode;

	@Column(name="CARD_PRODUCT_ID")
	private String cardProductId;

	@Column(name="CARD_STATUS")
	private String cardStatus;

	@Column(name="CARD_STATUS_CODE")
	private String cardStatusCode;

	@Column(name="CARD_TYPE")
	private String cardType;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="CUSTOMER_ID")
	private String customerId;

	private String descr;

	private String expirydate;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MAX_RETRIES")
	private BigDecimal maxRetries;

	private String nameoncard;

	@Column(name="PRODUCT_CATEGORY")
	private String productCategory;

	@Column(name="RELATIONSHIP_ID")
	private String relationshipId;

	@Column(name="RETRIES_LEFT")
	private BigDecimal retriesLeft;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblDebitCardRequest
	@ManyToOne
	@JoinColumn(name="DEBIT_CARD_REQUEST_ID")
	private TblDebitCardRequest tblDebitCardRequest;

	public TblDebitCardResponse() {
	}

	public long getDebitCardResponseId() {
		return this.debitCardResponseId;
	}

	public void setDebitCardResponseId(long debitCardResponseId) {
		this.debitCardResponseId = debitCardResponseId;
	}

	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCardCategory() {
		return this.cardCategory;
	}

	public void setCardCategory(String cardCategory) {
		this.cardCategory = cardCategory;
	}

	public String getCardId() {
		return this.cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getCardNumber() {
		return this.cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardProduct() {
		return this.cardProduct;
	}

	public void setCardProduct(String cardProduct) {
		this.cardProduct = cardProduct;
	}

	public String getCardProductCode() {
		return this.cardProductCode;
	}

	public void setCardProductCode(String cardProductCode) {
		this.cardProductCode = cardProductCode;
	}

	public String getCardProductId() {
		return this.cardProductId;
	}

	public void setCardProductId(String cardProductId) {
		this.cardProductId = cardProductId;
	}

	public String getCardStatus() {
		return this.cardStatus;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getCardStatusCode() {
		return this.cardStatusCode;
	}

	public void setCardStatusCode(String cardStatusCode) {
		this.cardStatusCode = cardStatusCode;
	}

	public String getCardType() {
		return this.cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
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

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getExpirydate() {
		return this.expirydate;
	}

	public void setExpirydate(String expirydate) {
		this.expirydate = expirydate;
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

	public BigDecimal getMaxRetries() {
		return this.maxRetries;
	}

	public void setMaxRetries(BigDecimal maxRetries) {
		this.maxRetries = maxRetries;
	}

	public String getNameoncard() {
		return this.nameoncard;
	}

	public void setNameoncard(String nameoncard) {
		this.nameoncard = nameoncard;
	}

	public String getProductCategory() {
		return this.productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getRelationshipId() {
		return this.relationshipId;
	}

	public void setRelationshipId(String relationshipId) {
		this.relationshipId = relationshipId;
	}

	public BigDecimal getRetriesLeft() {
		return this.retriesLeft;
	}

	public void setRetriesLeft(BigDecimal retriesLeft) {
		this.retriesLeft = retriesLeft;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public TblDebitCardRequest getTblDebitCardRequest() {
		return this.tblDebitCardRequest;
	}

	public void setTblDebitCardRequest(TblDebitCardRequest tblDebitCardRequest) {
		this.tblDebitCardRequest = tblDebitCardRequest;
	}

}