package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_COMMISSION_DOCS database table.
 * 
 */
@Entity
@Table(name="TBL_COMMISSION_DOCS")
@NamedQuery(name="TblCommissionDoc.findAll", query="SELECT t FROM TblCommissionDoc t")
public class TblCommissionDoc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_COMMISSION_DOCS_COMMISSIONDOCSID_GENERATOR", sequenceName="TBL_COMMISSION_DOCS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_COMMISSION_DOCS_COMMISSIONDOCSID_GENERATOR")
	@Column(name="COMMISSION_DOCS_ID")
	private long commissionDocsId;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private String status;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblCommissionProfile
	@ManyToOne
	@JoinColumn(name="COMMISSION_PROFILE_ID")
	private TblCommissionProfile tblCommissionProfile;

	//bi-directional many-to-one association to TblTransDoc
	@ManyToOne
	@JoinColumn(name="TRANS_DOCS_ID")
	private TblTransDoc tblTransDoc;

	public TblCommissionDoc() {
	}

	public long getCommissionDocsId() {
		return this.commissionDocsId;
	}

	public void setCommissionDocsId(long commissionDocsId) {
		this.commissionDocsId = commissionDocsId;
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

	public TblCommissionProfile getTblCommissionProfile() {
		return this.tblCommissionProfile;
	}

	public void setTblCommissionProfile(TblCommissionProfile tblCommissionProfile) {
		this.tblCommissionProfile = tblCommissionProfile;
	}

	public TblTransDoc getTblTransDoc() {
		return this.tblTransDoc;
	}

	public void setTblTransDoc(TblTransDoc tblTransDoc) {
		this.tblTransDoc = tblTransDoc;
	}

}