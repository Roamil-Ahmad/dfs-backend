package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_MW_REQUEST database table.
 * 
 */
@Entity
@Table(name="TBL_MW_REQUEST")
@NamedQuery(name="TblMwRequest.findAll", query="SELECT t FROM TblMwRequest t")
public class TblMwRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_MW_REQUEST_MWREQUESTID_GENERATOR", sequenceName="TBL_MW_REQUEST_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MW_REQUEST_MWREQUESTID_GENERATOR")
	@Column(name="MW_REQUEST_ID")
	private long mwRequestId;

	private String acqinstcode;

	private String cardacceptornamelocation;

	private String cardacceptorterminalid;

	private Date createdate;

	private BigDecimal createuser;

	private String datelocaltran;

	private String destinationimd;

	private String fromaccountcurrency;

	private String fromaccountnumber;

	private String fromaccounttype;

	private String fttype;

	private String identifier;

	private String merchanttype;

	private String originaltransactiondata;

	private String pospanentrymode;

	private String pospinentrymode;

	private String recorddata;

	private String relationshipid;

	@Column(name="\"RRN\"")
	private String rrn;

	private String sourceimd;

	private String stan;

	private String timelocaltran;

	private String toaccountcurrency;

	private String toaccountnumber;

	private String toaccounttype;

	private String transactionamount;

	private String transactioncode;

	private String transactioncurrency;

	private String transactionfee;

	private String transactionpurpose;

	private String transmissiondate;

	private String transmissiontime;

	private String udf1;

	private String udf2;

	private String udf3;

	private String udf4;

	private String udf5;

	private String utilitycompanyid;

	private String utilityconsumernumber;

	//bi-directional many-to-one association to TblMwRequestType
	@ManyToOne
	@JoinColumn(name="MW_REQUEST_TYPE_ID")
	private TblMwRequestType tblMwRequestType;

	//bi-directional many-to-one association to TblMwResponse
	@OneToMany(mappedBy="tblMwRequest")
	private List<TblMwResponse> tblMwResponses;

	//bi-directional many-to-one association to TblTransHead
	@OneToMany(mappedBy="tblMwRequest")
	private List<TblTransHead> tblTransHeads;

	public TblMwRequest() {
	}

	public long getMwRequestId() {
		return this.mwRequestId;
	}

	public void setMwRequestId(long mwRequestId) {
		this.mwRequestId = mwRequestId;
	}

	public String getAcqinstcode() {
		return this.acqinstcode;
	}

	public void setAcqinstcode(String acqinstcode) {
		this.acqinstcode = acqinstcode;
	}

	public String getCardacceptornamelocation() {
		return this.cardacceptornamelocation;
	}

	public void setCardacceptornamelocation(String cardacceptornamelocation) {
		this.cardacceptornamelocation = cardacceptornamelocation;
	}

	public String getCardacceptorterminalid() {
		return this.cardacceptorterminalid;
	}

	public void setCardacceptorterminalid(String cardacceptorterminalid) {
		this.cardacceptorterminalid = cardacceptorterminalid;
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

	public String getDatelocaltran() {
		return this.datelocaltran;
	}

	public void setDatelocaltran(String datelocaltran) {
		this.datelocaltran = datelocaltran;
	}

	public String getDestinationimd() {
		return this.destinationimd;
	}

	public void setDestinationimd(String destinationimd) {
		this.destinationimd = destinationimd;
	}

	public String getFromaccountcurrency() {
		return this.fromaccountcurrency;
	}

	public void setFromaccountcurrency(String fromaccountcurrency) {
		this.fromaccountcurrency = fromaccountcurrency;
	}

	public String getFromaccountnumber() {
		return this.fromaccountnumber;
	}

	public void setFromaccountnumber(String fromaccountnumber) {
		this.fromaccountnumber = fromaccountnumber;
	}

	public String getFromaccounttype() {
		return this.fromaccounttype;
	}

	public void setFromaccounttype(String fromaccounttype) {
		this.fromaccounttype = fromaccounttype;
	}

	public String getFttype() {
		return this.fttype;
	}

	public void setFttype(String fttype) {
		this.fttype = fttype;
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getMerchanttype() {
		return this.merchanttype;
	}

	public void setMerchanttype(String merchanttype) {
		this.merchanttype = merchanttype;
	}

	public String getOriginaltransactiondata() {
		return this.originaltransactiondata;
	}

	public void setOriginaltransactiondata(String originaltransactiondata) {
		this.originaltransactiondata = originaltransactiondata;
	}

	public String getPospanentrymode() {
		return this.pospanentrymode;
	}

	public void setPospanentrymode(String pospanentrymode) {
		this.pospanentrymode = pospanentrymode;
	}

	public String getPospinentrymode() {
		return this.pospinentrymode;
	}

	public void setPospinentrymode(String pospinentrymode) {
		this.pospinentrymode = pospinentrymode;
	}

	public String getRecorddata() {
		return this.recorddata;
	}

	public void setRecorddata(String recorddata) {
		this.recorddata = recorddata;
	}

	public String getRelationshipid() {
		return this.relationshipid;
	}

	public void setRelationshipid(String relationshipid) {
		this.relationshipid = relationshipid;
	}

	public String getRrn() {
		return this.rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getSourceimd() {
		return this.sourceimd;
	}

	public void setSourceimd(String sourceimd) {
		this.sourceimd = sourceimd;
	}

	public String getStan() {
		return this.stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}

	public String getTimelocaltran() {
		return this.timelocaltran;
	}

	public void setTimelocaltran(String timelocaltran) {
		this.timelocaltran = timelocaltran;
	}

	public String getToaccountcurrency() {
		return this.toaccountcurrency;
	}

	public void setToaccountcurrency(String toaccountcurrency) {
		this.toaccountcurrency = toaccountcurrency;
	}

	public String getToaccountnumber() {
		return this.toaccountnumber;
	}

	public void setToaccountnumber(String toaccountnumber) {
		this.toaccountnumber = toaccountnumber;
	}

	public String getToaccounttype() {
		return this.toaccounttype;
	}

	public void setToaccounttype(String toaccounttype) {
		this.toaccounttype = toaccounttype;
	}

	public String getTransactionamount() {
		return this.transactionamount;
	}

	public void setTransactionamount(String transactionamount) {
		this.transactionamount = transactionamount;
	}

	public String getTransactioncode() {
		return this.transactioncode;
	}

	public void setTransactioncode(String transactioncode) {
		this.transactioncode = transactioncode;
	}

	public String getTransactioncurrency() {
		return this.transactioncurrency;
	}

	public void setTransactioncurrency(String transactioncurrency) {
		this.transactioncurrency = transactioncurrency;
	}

	public String getTransactionfee() {
		return this.transactionfee;
	}

	public void setTransactionfee(String transactionfee) {
		this.transactionfee = transactionfee;
	}

	public String getTransactionpurpose() {
		return this.transactionpurpose;
	}

	public void setTransactionpurpose(String transactionpurpose) {
		this.transactionpurpose = transactionpurpose;
	}

	public String getTransmissiondate() {
		return this.transmissiondate;
	}

	public void setTransmissiondate(String transmissiondate) {
		this.transmissiondate = transmissiondate;
	}

	public String getTransmissiontime() {
		return this.transmissiontime;
	}

	public void setTransmissiontime(String transmissiontime) {
		this.transmissiontime = transmissiontime;
	}

	public String getUdf1() {
		return this.udf1;
	}

	public void setUdf1(String udf1) {
		this.udf1 = udf1;
	}

	public String getUdf2() {
		return this.udf2;
	}

	public void setUdf2(String udf2) {
		this.udf2 = udf2;
	}

	public String getUdf3() {
		return this.udf3;
	}

	public void setUdf3(String udf3) {
		this.udf3 = udf3;
	}

	public String getUdf4() {
		return this.udf4;
	}

	public void setUdf4(String udf4) {
		this.udf4 = udf4;
	}

	public String getUdf5() {
		return this.udf5;
	}

	public void setUdf5(String udf5) {
		this.udf5 = udf5;
	}

	public String getUtilitycompanyid() {
		return this.utilitycompanyid;
	}

	public void setUtilitycompanyid(String utilitycompanyid) {
		this.utilitycompanyid = utilitycompanyid;
	}

	public String getUtilityconsumernumber() {
		return this.utilityconsumernumber;
	}

	public void setUtilityconsumernumber(String utilityconsumernumber) {
		this.utilityconsumernumber = utilityconsumernumber;
	}

	public TblMwRequestType getTblMwRequestType() {
		return this.tblMwRequestType;
	}

	public void setTblMwRequestType(TblMwRequestType tblMwRequestType) {
		this.tblMwRequestType = tblMwRequestType;
	}

	public List<TblMwResponse> getTblMwResponses() {
		return this.tblMwResponses;
	}

	public void setTblMwResponses(List<TblMwResponse> tblMwResponses) {
		this.tblMwResponses = tblMwResponses;
	}

	public TblMwResponse addTblMwRespons(TblMwResponse tblMwRespons) {
		getTblMwResponses().add(tblMwRespons);
		tblMwRespons.setTblMwRequest(this);

		return tblMwRespons;
	}

	public TblMwResponse removeTblMwRespons(TblMwResponse tblMwRespons) {
		getTblMwResponses().remove(tblMwRespons);
		tblMwRespons.setTblMwRequest(null);

		return tblMwRespons;
	}

	public List<TblTransHead> getTblTransHeads() {
		return this.tblTransHeads;
	}

	public void setTblTransHeads(List<TblTransHead> tblTransHeads) {
		this.tblTransHeads = tblTransHeads;
	}

	public TblTransHead addTblTransHead(TblTransHead tblTransHead) {
		getTblTransHeads().add(tblTransHead);
		tblTransHead.setTblMwRequest(this);

		return tblTransHead;
	}

	public TblTransHead removeTblTransHead(TblTransHead tblTransHead) {
		getTblTransHeads().remove(tblTransHead);
		tblTransHead.setTblMwRequest(null);

		return tblTransHead;
	}

}