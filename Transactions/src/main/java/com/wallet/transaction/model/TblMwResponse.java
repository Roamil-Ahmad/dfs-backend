package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_MW_RESPONSE database table.
 * 
 */
@Entity
@Table(name="TBL_MW_RESPONSE")
@NamedQuery(name="TblMwResponse.findAll", query="SELECT t FROM TblMwResponse t")
public class TblMwResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_MW_RESPONSE_MWRESPONSEID_GENERATOR", sequenceName="TBL_MW_RESPONSE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MW_RESPONSE_MWRESPONSEID_GENERATOR")
	@Column(name="MW_RESPONSE_ID")
	private long mwResponseId;

	private String actualbalance;

	private String authidresponse;

	private String availablebalance;

	private String branchcode;

	private String branchname;

	@Column(name="\"CHECKPOINT\"")
	private String checkpoint;

	private Date createdate;

	private BigDecimal createuser;

	private String errorresponse;

	private String mobilenumber;

	private String recorddata;

	private String transactiondata;

	private String udf1;

	//bi-directional many-to-one association to TblMwRequest
	@ManyToOne
	@JoinColumn(name="MW_REQUEST_ID")
	private TblMwRequest tblMwRequest;

	public TblMwResponse() {
	}

	public long getMwResponseId() {
		return this.mwResponseId;
	}

	public void setMwResponseId(long mwResponseId) {
		this.mwResponseId = mwResponseId;
	}

	public String getActualbalance() {
		return this.actualbalance;
	}

	public void setActualbalance(String actualbalance) {
		this.actualbalance = actualbalance;
	}

	public String getAuthidresponse() {
		return this.authidresponse;
	}

	public void setAuthidresponse(String authidresponse) {
		this.authidresponse = authidresponse;
	}

	public String getAvailablebalance() {
		return this.availablebalance;
	}

	public void setAvailablebalance(String availablebalance) {
		this.availablebalance = availablebalance;
	}

	public String getBranchcode() {
		return this.branchcode;
	}

	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}

	public String getBranchname() {
		return this.branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getCheckpoint() {
		return this.checkpoint;
	}

	public void setCheckpoint(String checkpoint) {
		this.checkpoint = checkpoint;
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

	public String getErrorresponse() {
		return this.errorresponse;
	}

	public void setErrorresponse(String errorresponse) {
		this.errorresponse = errorresponse;
	}

	public String getMobilenumber() {
		return this.mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getRecorddata() {
		return this.recorddata;
	}

	public void setRecorddata(String recorddata) {
		this.recorddata = recorddata;
	}

	public String getTransactiondata() {
		return this.transactiondata;
	}

	public void setTransactiondata(String transactiondata) {
		this.transactiondata = transactiondata;
	}

	public String getUdf1() {
		return this.udf1;
	}

	public void setUdf1(String udf1) {
		this.udf1 = udf1;
	}

	public TblMwRequest getTblMwRequest() {
		return this.tblMwRequest;
	}

	public void setTblMwRequest(TblMwRequest tblMwRequest) {
		this.tblMwRequest = tblMwRequest;
	}

}