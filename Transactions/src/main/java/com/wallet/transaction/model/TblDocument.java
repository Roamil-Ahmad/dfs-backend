package com.wallet.transaction.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_DOCUMENT database table.
 * 
 */
@Entity
@Table(name="TBL_DOCUMENT")
@NamedQuery(name="TblDocument.findAll", query="SELECT t FROM TblDocument t")
public class TblDocument implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_DOCUMENT_DOCUMENTID_GENERATOR", sequenceName="TBL_DOCUMENT_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_DOCUMENT_DOCUMENTID_GENERATOR")
	@Column(name="DOCUMENT_ID")
	private long documentId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DOCUMENT_EXT")
	private String documentExt;

	@Column(name="DOCUMENT_PATH")
	private String documentPath;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to LkpDocumentType
	@ManyToOne
	@JoinColumn(name="DOCUMENT_TYPE_ID")
	private LkpDocumentType lkpDocumentType;

	//bi-directional many-to-one association to TblAppUser
	@ManyToOne
	@JoinColumn(name="APP_USER_ID")
	private TblAppUser tblAppUser;

	//bi-directional many-to-one association to TblAppUser
	@ManyToOne
	@JoinColumn(name="ACCOUNT_LEVEL_ID")
	private TblAccountLevel tblAccountLevel;

	public TblDocument() {
	}

	public long getDocumentId() {
		return this.documentId;
	}

	public void setDocumentId(long documentId) {
		this.documentId = documentId;
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

	public String getDocumentExt() {
		return this.documentExt;
	}

	public void setDocumentExt(String documentExt) {
		this.documentExt = documentExt;
	}

	public String getDocumentPath() {
		return this.documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
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

	public LkpDocumentType getLkpDocumentType() {
		return this.lkpDocumentType;
	}

	public void setLkpDocumentType(LkpDocumentType lkpDocumentType) {
		this.lkpDocumentType = lkpDocumentType;
	}

	public TblAppUser getTblAppUser() {
		return this.tblAppUser;
	}

	public void setTblAppUser(TblAppUser tblAppUser) {
		this.tblAppUser = tblAppUser;
	}

	public TblAccountLevel getTblAccountLevel() {
		return tblAccountLevel;
	}

	public void setTblAccountLevel(TblAccountLevel tblAccountLevel) {
		this.tblAccountLevel = tblAccountLevel;
	}
}