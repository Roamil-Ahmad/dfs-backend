package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the LKP_DOCUMENT_TYPE database table.
 * 
 */
@Entity
@Table(name="LKP_DOCUMENT_TYPE")
@NamedQuery(name="LkpDocumentType.findAll", query="SELECT l FROM LkpDocumentType l")
public class LkpDocumentType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_DOCUMENT_TYPE_DOCUMENTTYPEID_GENERATOR", sequenceName="LKP_DOCUMENT_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_DOCUMENT_TYPE_DOCUMENTTYPEID_GENERATOR")
	@Column(name="DOCUMENT_TYPE_ID")
	private long documentTypeId;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DOCUMENT_TYPE_CODE")
	private String documentTypeCode;

	@Column(name="DOCUMENT_TYPE_DESCR")
	private String documentTypeDescr;

	@Column(name="DOCUMENT_TYPE_NAME")
	private String documentTypeName;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblDocument
	@OneToMany(mappedBy="lkpDocumentType")
	private List<TblDocument> tblDocuments;

	public LkpDocumentType() {
	}

	public long getDocumentTypeId() {
		return this.documentTypeId;
	}

	public void setDocumentTypeId(long documentTypeId) {
		this.documentTypeId = documentTypeId;
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

	public String getDocumentTypeCode() {
		return this.documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	public String getDocumentTypeDescr() {
		return this.documentTypeDescr;
	}

	public void setDocumentTypeDescr(String documentTypeDescr) {
		this.documentTypeDescr = documentTypeDescr;
	}

	public String getDocumentTypeName() {
		return this.documentTypeName;
	}

	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
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

	public List<TblDocument> getTblDocuments() {
		return this.tblDocuments;
	}

	public void setTblDocuments(List<TblDocument> tblDocuments) {
		this.tblDocuments = tblDocuments;
	}

	public TblDocument addTblDocument(TblDocument tblDocument) {
		getTblDocuments().add(tblDocument);
		tblDocument.setLkpDocumentType(this);

		return tblDocument;
	}

	public TblDocument removeTblDocument(TblDocument tblDocument) {
		getTblDocuments().remove(tblDocument);
		tblDocument.setLkpDocumentType(null);

		return tblDocument;
	}

}