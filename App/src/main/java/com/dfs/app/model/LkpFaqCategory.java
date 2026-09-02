package com.dfs.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the LKP_FAQ_CATEGORY database table.
 * 
 */
@Entity
@Table(name="LKP_FAQ_CATEGORY")
@NamedQuery(name="LkpFaqCategory.findAll", query="SELECT l FROM LkpFaqCategory l")
public class LkpFaqCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_FAQ_CATEGORY_FAQCATEGORYID_GENERATOR", sequenceName="LKP_FAQ_CATEGORY_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_FAQ_CATEGORY_FAQCATEGORYID_GENERATOR")
	@Column(name="FAQ_CATEGORY_ID")
	@JsonIgnore
	private long faqCategoryId;
	@JsonIgnore
	private Timestamp createdate;
	@JsonIgnore
	private BigDecimal createuser;

	@Column(name="FAQ_CATEGORY")
	private String faqCategory;
	@JsonIgnore
	@Column(name="IS_ACTIVE")
	private String isActive;

	//bi-directional many-to-one association to TblFaq
	@OneToMany(mappedBy="lkpFaqCategory")
	private List<TblFaq> tblFaqs;

	public LkpFaqCategory() {
	}

	public long getFaqCategoryId() {
		return this.faqCategoryId;
	}

	public void setFaqCategoryId(long faqCategoryId) {
		this.faqCategoryId = faqCategoryId;
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

	public String getFaqCategory() {
		return this.faqCategory;
	}

	public void setFaqCategory(String faqCategory) {
		this.faqCategory = faqCategory;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public List<TblFaq> getTblFaqs() {
		return this.tblFaqs;
	}

	public void setTblFaqs(List<TblFaq> tblFaqs) {
		this.tblFaqs = tblFaqs;
	}

}