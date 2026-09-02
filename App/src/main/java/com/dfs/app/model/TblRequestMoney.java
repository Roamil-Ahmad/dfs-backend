package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_REQUEST_MONEY database table.
 * 
 */
@Entity
@Table(name="TBL_REQUEST_MONEY")
@NamedQuery(name="TblRequestMoney.findAll", query="SELECT t FROM TblRequestMoney t")
public class TblRequestMoney implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_REQUEST_MONEY_REQUESTMONEYID_GENERATOR", sequenceName="TBL_REQUEST_MONEY_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_REQUEST_MONEY_REQUESTMONEYID_GENERATOR")
	@Column(name="REQUEST_MONEY_ID")
	private long requestMoneyId;

	private BigDecimal amount;

	private String comments;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private String status;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblAccount
	@ManyToOne
	@JoinColumn(name="REQUESTER_ID")
	private TblAccount tblAccount1;

	//bi-directional many-to-one association to TblAccount
	@ManyToOne
	@JoinColumn(name="REQUESTEE_ID")
	private TblAccount tblAccount2;

	public TblRequestMoney() {
	}

	public long getRequestMoneyId() {
		return this.requestMoneyId;
	}

	public void setRequestMoneyId(long requestMoneyId) {
		this.requestMoneyId = requestMoneyId;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public TblAccount getTblAccount1() {
		return this.tblAccount1;
	}

	public void setTblAccount1(TblAccount tblAccount1) {
		this.tblAccount1 = tblAccount1;
	}

	public TblAccount getTblAccount2() {
		return this.tblAccount2;
	}

	public void setTblAccount2(TblAccount tblAccount2) {
		this.tblAccount2 = tblAccount2;
	}

}