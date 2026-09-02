package com.mfs.pricingprofile.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TBL_TRANS_CHARGES_DOCS database table.
 * 
 */
@Entity
@Table(name="TBL_TRANS_CHARGES_DOCS")
@NamedQuery(name="TblTransChargesDoc.findAll", query="SELECT t FROM TblTransChargesDoc t")
public class TblTransChargesDoc extends TransChargesCommonEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TRANS_CHARGES_DOCS_TRANSCHARGESDOCSID_GENERATOR", sequenceName="TBL_TRANS_CHARGES_DOCS_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TRANS_CHARGES_DOCS_TRANSCHARGESDOCSID_GENERATOR")
	@Column(name="TRANS_CHARGES_DOCS_ID")
	private long transChargesDocsId;

	@Column(name = "TRANS_DOCS_ID")
	private BigDecimal transDocsId;

	public long getTransChargesDocsId() {
		return transChargesDocsId;
	}

	public void setTransChargesDocsId(long transChargesDocsId) {
		this.transChargesDocsId = transChargesDocsId;
	}

	public BigDecimal getTransDocsId() {
		return transDocsId;
	}

	public void setTransDocsId(BigDecimal transDocsId) {
		this.transDocsId = transDocsId;
	}
}