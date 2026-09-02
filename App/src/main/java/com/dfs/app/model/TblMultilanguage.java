package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_MULTILANGUAGE database table.
 * 
 */
@Entity
@Table(name="TBL_MULTILANGUAGE")
@NamedQuery(name="TblMultilanguage.findAll", query="SELECT t FROM TblMultilanguage t")
public class TblMultilanguage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_MULTILANGUAGE_MULTILANGUAGEID_GENERATOR", sequenceName="TBL_MULTILANGUAGE_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MULTILANGUAGE_MULTILANGUAGEID_GENERATOR")
	@Column(name="MULTILANGUAGE_ID")
	private long multilanguageId;

	@Column(name="APP_SCREEN_NAME")
	private String appScreenName;

	@Column(name="COLUMN_NAME")
	private String columnName;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="FORM_NAME")
	private String formName;

	@Column(name="REF_ID")
	private BigDecimal refId;

	@Column(name="TABLE_NAME")
	private String tableName;

	@Column(name="VALUE")
	private String value;

	//bi-directional many-to-one association to LkpLanguage
	@ManyToOne
	@JoinColumn(name="LANGUAGE_ID")
	private LkpLanguage lkpLanguage;

	public TblMultilanguage() {
	}

	public long getMultilanguageId() {
		return this.multilanguageId;
	}

	public void setMultilanguageId(long multilanguageId) {
		this.multilanguageId = multilanguageId;
	}

	public String getAppScreenName() {
		return this.appScreenName;
	}

	public void setAppScreenName(String appScreenName) {
		this.appScreenName = appScreenName;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
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

	public String getFormName() {
		return this.formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public BigDecimal getRefId() {
		return this.refId;
	}

	public void setRefId(BigDecimal refId) {
		this.refId = refId;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LkpLanguage getLkpLanguage() {
		return this.lkpLanguage;
	}

	public void setLkpLanguage(LkpLanguage lkpLanguage) {
		this.lkpLanguage = lkpLanguage;
	}

}