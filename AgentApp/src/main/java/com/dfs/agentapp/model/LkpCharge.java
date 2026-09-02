package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the LKP_CHARGES database table.
 * 
 */
@Entity
@Table(name="LKP_CHARGES")
@NamedQuery(name="LkpCharge.findAll", query="SELECT l FROM LkpCharge l")
public class LkpCharge implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_CHARGES_CHARGESID_GENERATOR", sequenceName="LKP_CHARGES_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_CHARGES_CHARGESID_GENERATOR")
	@Column(name="CHARGES_ID")
	private long chargesId;

	@Column(name="CHARGES_CODE")
	private String chargesCode;

	@Column(name="CHARGES_DESCR")
	private String chargesDescr;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblTransDetail
	@OneToMany(mappedBy="lkpCharge")
	private List<TblTransDetail> tblTransDetails;

	public LkpCharge() {
	}

	public long getChargesId() {
		return this.chargesId;
	}

	public void setChargesId(long chargesId) {
		this.chargesId = chargesId;
	}

	public String getChargesCode() {
		return this.chargesCode;
	}

	public void setChargesCode(String chargesCode) {
		this.chargesCode = chargesCode;
	}

	public String getChargesDescr() {
		return this.chargesDescr;
	}

	public void setChargesDescr(String chargesDescr) {
		this.chargesDescr = chargesDescr;
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

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

	public List<TblTransDetail> getTblTransDetails() {
		return this.tblTransDetails;
	}

	public void setTblTransDetails(List<TblTransDetail> tblTransDetails) {
		this.tblTransDetails = tblTransDetails;
	}

	public TblTransDetail addTblTransDetail(TblTransDetail tblTransDetail) {
		getTblTransDetails().add(tblTransDetail);
		tblTransDetail.setLkpCharge(this);

		return tblTransDetail;
	}

	public TblTransDetail removeTblTransDetail(TblTransDetail tblTransDetail) {
		getTblTransDetails().remove(tblTransDetail);
		tblTransDetail.setLkpCharge(null);

		return tblTransDetail;
	}

}