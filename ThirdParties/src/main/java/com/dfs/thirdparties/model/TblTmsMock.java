package com.dfs.thirdparties.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_TMS_MOCK database table.
 * 
 */
@Entity
@Table(name="TBL_TMS_MOCK")
@NamedQuery(name="TblTmsMock.findAll", query="SELECT t FROM TblTmsMock t")
public class TblTmsMock implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TMS_MOCK_TMSMOCKID_GENERATOR", sequenceName="TBL_TMS_MOCK_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TMS_MOCK_TMSMOCKID_GENERATOR")
	@Column(name="TMS_MOCK_ID")
	private long tmsMockId;
	@Column(name="NID_NO")

	private String cnic;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private String name;

	private BigDecimal updateindex;

	public TblTmsMock() {
	}

	public long getTmsMockId() {
		return this.tmsMockId;
	}

	public void setTmsMockId(long tmsMockId) {
		this.tmsMockId = tmsMockId;
	}

	public String getCnic() {
		return this.cnic;
	}

	public void setCnic(String cnic) {
		this.cnic = cnic;
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