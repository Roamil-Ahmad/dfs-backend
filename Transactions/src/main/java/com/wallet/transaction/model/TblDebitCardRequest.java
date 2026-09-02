package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_DEBIT_CARD_REQUEST database table.
 * 
 */
@Entity
@Table(name="TBL_DEBIT_CARD_REQUEST")
@NamedQuery(name="TblDebitCardRequest.findAll", query="SELECT t FROM TblDebitCardRequest t")
public class TblDebitCardRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_DEBIT_CARD_REQUEST_DEBITCARDREQUESTID_GENERATOR", sequenceName="TBL_DEBIT_CARD_REQUEST_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_DEBIT_CARD_REQUEST_DEBITCARDREQUESTID_GENERATOR")
	@Column(name="DEBIT_CARD_REQUEST_ID")
	private long debitCardRequestId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DELIVERY_ADDRESS")
	private String deliveryAddress;

	@Column(name="DELIVERY_TYPE")
	private String deliveryType;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private String nameoncard;

	private String productcode;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpBranch
	@ManyToOne
	@JoinColumn(name="BRANCH_ID")
	private LkpBranch lkpBranch;

	//bi-directional many-to-one association to LkpDebitCardType
	@ManyToOne
	@JoinColumn(name="DEBIT_CARD_TYPE_ID")
	private LkpDebitCardType lkpDebitCardType;

	//bi-directional many-to-one association to TblAccount
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TblAccount tblAccount;

	//bi-directional many-to-one association to TblTransHead
	@ManyToOne
	@JoinColumn(name="TRANS_HEAD_ID")
	private TblTransHead tblTransHead;

	//bi-directional many-to-one association to TblDebitCardResponse
	@OneToMany(mappedBy="tblDebitCardRequest")
	private List<TblDebitCardResponse> tblDebitCardResponses;

	public TblDebitCardRequest() {
	}

	public long getDebitCardRequestId() {
		return this.debitCardRequestId;
	}

	public void setDebitCardRequestId(long debitCardRequestId) {
		this.debitCardRequestId = debitCardRequestId;
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

	public String getDeliveryAddress() {
		return this.deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDeliveryType() {
		return this.deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
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

	public String getNameoncard() {
		return this.nameoncard;
	}

	public void setNameoncard(String nameoncard) {
		this.nameoncard = nameoncard;
	}

	public String getProductcode() {
		return this.productcode;
	}

	public void setProductcode(String productcode) {
		this.productcode = productcode;
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

	public LkpDebitCardType getLkpDebitCardType() {
		return this.lkpDebitCardType;
	}

	public void setLkpDebitCardType(LkpDebitCardType lkpDebitCardType) {
		this.lkpDebitCardType = lkpDebitCardType;
	}

	public TblAccount getTblAccount() {
		return this.tblAccount;
	}

	public void setTblAccount(TblAccount tblAccount) {
		this.tblAccount = tblAccount;
	}

	public TblTransHead getTblTransHead() {
		return this.tblTransHead;
	}

	public void setTblTransHead(TblTransHead tblTransHead) {
		this.tblTransHead = tblTransHead;
	}

	public List<TblDebitCardResponse> getTblDebitCardResponses() {
		return this.tblDebitCardResponses;
	}

	public void setTblDebitCardResponses(List<TblDebitCardResponse> tblDebitCardResponses) {
		this.tblDebitCardResponses = tblDebitCardResponses;
	}

	public TblDebitCardResponse addTblDebitCardRespons(TblDebitCardResponse tblDebitCardRespons) {
		getTblDebitCardResponses().add(tblDebitCardRespons);
		tblDebitCardRespons.setTblDebitCardRequest(this);

		return tblDebitCardRespons;
	}

	public TblDebitCardResponse removeTblDebitCardRespons(TblDebitCardResponse tblDebitCardRespons) {
		getTblDebitCardResponses().remove(tblDebitCardRespons);
		tblDebitCardRespons.setTblDebitCardRequest(null);

		return tblDebitCardRespons;
	}

}