package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the LKP_TRANS_PURPOSE database table.
 * 
 */
@Entity
@Table(name="LKP_TRANS_PURPOSE")
@NamedQuery(name="LkpTransPurpose.findAll", query="SELECT l FROM LkpTransPurpose l")
public class LkpTransPurpose implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_TRANS_PURPOSE_TRANSPURPOSEID_GENERATOR", sequenceName="LKP_TRANS_PURPOSE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_TRANS_PURPOSE_TRANSPURPOSEID_GENERATOR")
	@Column(name="TRANS_PURPOSE_ID")
	private long transPurposeId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="TRANS_PURPOSE_CODE")
	private String transPurposeCode;

	@Column(name="TRANS_PURPOSE_DESCR")
	private String transPurposeDescr;

	private BigDecimal updateindex;

	public LkpTransPurpose() {
	}

	public long getTransPurposeId() {
		return this.transPurposeId;
	}

	public void setTransPurposeId(long transPurposeId) {
		this.transPurposeId = transPurposeId;
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

	public String getTransPurposeCode() {
		return this.transPurposeCode;
	}

	public void setTransPurposeCode(String transPurposeCode) {
		this.transPurposeCode = transPurposeCode;
	}

	public String getTransPurposeDescr() {
		return this.transPurposeDescr;
	}

	public void setTransPurposeDescr(String transPurposeDescr) {
		this.transPurposeDescr = transPurposeDescr;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}