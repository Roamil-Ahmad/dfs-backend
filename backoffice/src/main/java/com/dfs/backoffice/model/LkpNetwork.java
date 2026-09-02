package com.dfs.backoffice.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the LKP_NETWORK database table.
 * 
 */
@Entity
@Table(name="LKP_NETWORK")
@NamedQuery(name="LkpNetwork.findAll", query="SELECT l FROM LkpNetwork l")
public class LkpNetwork implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_NETWORK_NETWORKID_GENERATOR", sequenceName="LKP_NETWORK_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_NETWORK_NETWORKID_GENERATOR")
	@Column(name="NETWORK_ID")
	private long networkId;

	private Timestamp createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Timestamp lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="NETWORK_CODE")
	private String networkCode;

	@Column(name="NETWORK_DESCR")
	private String networkDescr;

	@Column(name="SORT_SEQ")
	private BigDecimal sortSeq;

	private BigDecimal updateindex;

	public long getNetworkId() {
		return this.networkId;
	}

	public void setNetworkId(long networkId) {
		this.networkId = networkId;
	}

	public Timestamp getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Timestamp createdate) {
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

	public Timestamp getLastupdatedate() {
		return this.lastupdatedate;
	}

	public void setLastupdatedate(Timestamp lastupdatedate) {
		this.lastupdatedate = lastupdatedate;
	}

	public BigDecimal getLastupdateuser() {
		return this.lastupdateuser;
	}

	public void setLastupdateuser(BigDecimal lastupdateuser) {
		this.lastupdateuser = lastupdateuser;
	}

	public String getNetworkCode() {
		return this.networkCode;
	}

	public void setNetworkCode(String networkCode) {
		this.networkCode = networkCode;
	}

	public String getNetworkDescr() {
		return this.networkDescr;
	}

	public void setNetworkDescr(String networkDescr) {
		this.networkDescr = networkDescr;
	}

	public BigDecimal getSortSeq() {
		return this.sortSeq;
	}

	public void setSortSeq(BigDecimal sortSeq) {
		this.sortSeq = sortSeq;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}
}