package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;


/**
 * The persistent class for the LKP_TRANS_MODE database table.
 * 
 */
@Entity
@Table(name="LKP_TRANS_MODE")
@NamedQuery(name="LkpTransMode.findAll", query="SELECT l FROM LkpTransMode l")
public class LkpTransMode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_TRANS_MODE_TRANSMODEID_GENERATOR", sequenceName="LKP_TRANS_MODE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_TRANS_MODE_TRANSMODEID_GENERATOR")
	@Column(name="TRANS_MODE_ID")
	private long transModeId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="TRANS_MODE_CODE")
	private String transModeCode;

	@Column(name="TRANS_MODE_DESCR")
	private String transModeDescr;

	private BigDecimal updateindex;

	public LkpTransMode() {
	}

	public long getTransModeId() {
		return this.transModeId;
	}

	public void setTransModeId(long transModeId) {
		this.transModeId = transModeId;
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

	public String getTransModeCode() {
		return this.transModeCode;
	}

	public void setTransModeCode(String transModeCode) {
		this.transModeCode = transModeCode;
	}

	public String getTransModeDescr() {
		return this.transModeDescr;
	}

	public void setTransModeDescr(String transModeDescr) {
		this.transModeDescr = transModeDescr;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}