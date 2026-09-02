package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_OFAC database table.
 * 
 */
@Entity
@Table(name="TBL_OFAC")
@NamedQuery(name="TblOfac.findAll", query="SELECT t FROM TblOfac t")
public class TblOfac implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_OFAC_OFACID_GENERATOR", sequenceName="TBL_OFAC_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_OFAC_OFACID_GENERATOR")
	@Column(name="OFAC_ID")
	private long ofacId;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private String name;

	private BigDecimal updateindex;

	public TblOfac() {
	}

	public long getOfacId() {
		return this.ofacId;
	}

	public void setOfacId(long ofacId) {
		this.ofacId = ofacId;
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

	public Date getLastupdatedate() {
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

}