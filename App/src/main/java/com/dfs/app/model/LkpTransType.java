package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the LKP_TRANS_TYPE database table.
 * 
 */
@Entity
@Table(name="LKP_TRANS_TYPE")
@NamedQuery(name="LkpTransType.findAll", query="SELECT l FROM LkpTransType l")
public class LkpTransType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_TRANS_TYPE_TRANSTYPEID_GENERATOR", sequenceName="LKP_TRANS_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_TRANS_TYPE_TRANSTYPEID_GENERATOR")
	@Column(name="TRANS_TYPE_ID")
	private long transTypeId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="TRANS_TYPE_CODE")
	private String transTypeCode;

	@Column(name="TRANS_TYPE_DESCR")
	private String transTypeDescr;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblTransDoc
	@OneToMany(mappedBy="lkpTransType")
	private List<TblTransDoc> tblTransDocs;

	public LkpTransType() {
	}

	public long getTransTypeId() {
		return this.transTypeId;
	}

	public void setTransTypeId(long transTypeId) {
		this.transTypeId = transTypeId;
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

	public String getTransTypeCode() {
		return this.transTypeCode;
	}

	public void setTransTypeCode(String transTypeCode) {
		this.transTypeCode = transTypeCode;
	}

	public String getTransTypeDescr() {
		return this.transTypeDescr;
	}

	public void setTransTypeDescr(String transTypeDescr) {
		this.transTypeDescr = transTypeDescr;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public List<TblTransDoc> getTblTransDocs() {
		return this.tblTransDocs;
	}

	public void setTblTransDocs(List<TblTransDoc> tblTransDocs) {
		this.tblTransDocs = tblTransDocs;
	}

	public TblTransDoc addTblTransDoc(TblTransDoc tblTransDoc) {
		getTblTransDocs().add(tblTransDoc);
		tblTransDoc.setLkpTransType(this);

		return tblTransDoc;
	}

	public TblTransDoc removeTblTransDoc(TblTransDoc tblTransDoc) {
		getTblTransDocs().remove(tblTransDoc);
		tblTransDoc.setLkpTransType(null);

		return tblTransDoc;
	}

}