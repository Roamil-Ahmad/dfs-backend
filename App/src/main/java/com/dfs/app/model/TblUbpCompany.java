package com.dfs.app.model;

import java.io.Serializable;
import javax.persistence.*;import java.util.Date;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the TBL_UBP_COMPANIES database table.
 * 
 */
@Entity
@Table(name="TBL_UBP_COMPANIES")
@NamedQuery(name="TblUbpCompany.findAll", query="SELECT t FROM TblUbpCompany t")
public class TblUbpCompany implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_UBP_COMPANIES_UBPCOMPANIESID_GENERATOR", sequenceName="TBL_UBP_COMPANIES_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_UBP_COMPANIES_UBPCOMPANIESID_GENERATOR")
	@Column(name="UBP_COMPANIES_ID")
	private long ubpCompaniesId;

	private String code;

	@Lob
	@Column(name="CODE_ENCR")
	private String codeEncr;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="DEPTH")
	private BigDecimal depth;

	private String descr;

	private String format;

	private String identifier;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="MAIN_MENU")
	private String mainMenu;

	private String name;

	@Lob
	@Column(name="NAME_ENCR")
	private String nameEncr;

	@Column(name="\"PATH\"")
	private String path;

	@Column(name="\"PREFIX\"")
	private String prefix;

	private String status;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblTransDoc
	@ManyToOne
	@JoinColumn(name="TRANS_DOCS_ID")
	private TblTransDoc tblTransDoc;

	//bi-directional many-to-one association to TblUbpCompany
	@ManyToOne
	@JoinColumn(name="PARENT_ID")
	private TblUbpCompany tblUbpCompany;

	//bi-directional many-to-one association to TblUbpCompany
	@OneToMany(mappedBy="tblUbpCompany")
	private List<TblUbpCompany> tblUbpCompanies;

	public TblUbpCompany() {
	}

	public long getUbpCompaniesId() {
		return this.ubpCompaniesId;
	}

	public void setUbpCompaniesId(long ubpCompaniesId) {
		this.ubpCompaniesId = ubpCompaniesId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeEncr() {
		return this.codeEncr;
	}

	public void setCodeEncr(String codeEncr) {
		this.codeEncr = codeEncr;
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

	public BigDecimal getDepth() {
		return this.depth;
	}

	public void setDepth(BigDecimal depth) {
		this.depth = depth;
	}

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
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

	public String getMainMenu() {
		return this.mainMenu;
	}

	public void setMainMenu(String mainMenu) {
		this.mainMenu = mainMenu;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEncr() {
		return this.nameEncr;
	}

	public void setNameEncr(String nameEncr) {
		this.nameEncr = nameEncr;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
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

	public TblTransDoc getTblTransDoc() {
		return this.tblTransDoc;
	}

	public void setTblTransDoc(TblTransDoc tblTransDoc) {
		this.tblTransDoc = tblTransDoc;
	}

	public TblUbpCompany getTblUbpCompany() {
		return this.tblUbpCompany;
	}

	public void setTblUbpCompany(TblUbpCompany tblUbpCompany) {
		this.tblUbpCompany = tblUbpCompany;
	}

	public List<TblUbpCompany> getTblUbpCompanies() {
		return this.tblUbpCompanies;
	}

	public void setTblUbpCompanies(List<TblUbpCompany> tblUbpCompanies) {
		this.tblUbpCompanies = tblUbpCompanies;
	}

	public TblUbpCompany addTblUbpCompany(TblUbpCompany tblUbpCompany) {
		getTblUbpCompanies().add(tblUbpCompany);
		tblUbpCompany.setTblUbpCompany(this);

		return tblUbpCompany;
	}

	public TblUbpCompany removeTblUbpCompany(TblUbpCompany tblUbpCompany) {
		getTblUbpCompanies().remove(tblUbpCompany);
		tblUbpCompany.setTblUbpCompany(null);

		return tblUbpCompany;
	}

}