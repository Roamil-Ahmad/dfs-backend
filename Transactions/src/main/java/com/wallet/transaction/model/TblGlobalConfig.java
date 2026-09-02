package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_GLOBAL_CONFIG database table.
 * 
 */
@Entity
@Table(name="TBL_GLOBAL_CONFIG")
@NamedQuery(name="TblGlobalConfig.findAll", query="SELECT t FROM TblGlobalConfig t")
public class TblGlobalConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_GLOBAL_CONFIG_GLOBALCONFIGID_GENERATOR", sequenceName="TBL_GLOBAL_CONFIG_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_GLOBAL_CONFIG_GLOBALCONFIGID_GENERATOR")
	@Column(name="GLOBAL_CONFIG_ID")
	private String globalConfigId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="KEY_NAME")
	private String keyName;

	@Column(name="KEY_VALUE")
	private String keyValue;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	public TblGlobalConfig() {
	}

	public String getGlobalConfigId() {
		return this.globalConfigId;
	}

	public void setGlobalConfigId(String globalConfigId) {
		this.globalConfigId = globalConfigId;
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

	public String getKeyName() {
		return this.keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyValue() {
		return this.keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
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

}