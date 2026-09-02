package com.workflow.modal;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_MC_REQUEST_DETAIL database table.
 * 
 */
@Entity
@Table(name="TBL_MC_REQUEST_DETAIL")
@NamedQuery(name="TblMcRequestDetail.findAll", query="SELECT t FROM TblMcRequestDetail t")
public class TblMcRequestDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_MC_REQUEST_DETAIL_MCREQUESTDETAILID_GENERATOR", sequenceName="TBL_MC_REQUEST_DETAIL_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_MC_REQUEST_DETAIL_MCREQUESTDETAILID_GENERATOR")
	@Column(name="MC_REQUEST_DETAIL_ID")
	private long mcRequestDetailId;

	@Column(name="COLUMN_ALIAS")
	private String columnAlias;

	@Column(name="COLUMN_NAME")
	private String columnName;

	@Temporal(TemporalType.DATE)
	private Date createdate;

	private BigDecimal createuser;

	@Temporal(TemporalType.DATE)
	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="NEW_VALUE_DB")
	private String newValueDb;

	@Column(name="NEW_VALUE_UI")
	private String newValueUi;

	@Column(name="OLD_VALUE_DB")
	private String oldValueDb;

	@Column(name="OLD_VALUE_UI")
	private String oldValueUi;

	private BigDecimal updateindex;

	//bi-directional many-to-one association to TblMcRequest
	@ManyToOne
	@JoinColumn(name="MC_REQUEST_ID")
	private TblMcRequest tblMcRequest;


	public long getMcRequestDetailId() {
		return this.mcRequestDetailId;
	}

	public void setMcRequestDetailId(long mcRequestDetailId) {
		this.mcRequestDetailId = mcRequestDetailId;
	}

	public String getColumnAlias() {
		return this.columnAlias;
	}

	public void setColumnAlias(String columnAlias) {
		this.columnAlias = columnAlias;
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

	public String getNewValueDb() {
		return this.newValueDb;
	}

	public void setNewValueDb(String newValueDb) {
		this.newValueDb = newValueDb;
	}

	public String getNewValueUi() {
		return this.newValueUi;
	}

	public void setNewValueUi(String newValueUi) {
		this.newValueUi = newValueUi;
	}

	public String getOldValueDb() {
		return this.oldValueDb;
	}

	public void setOldValueDb(String oldValueDb) {
		this.oldValueDb = oldValueDb;
	}

	public String getOldValueUi() {
		return this.oldValueUi;
	}

	public void setOldValueUi(String oldValueUi) {
		this.oldValueUi = oldValueUi;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public TblMcRequest getTblMcRequest() {
		return this.tblMcRequest;
	}

	public void setTblMcRequest(TblMcRequest tblMcRequest) {
		this.tblMcRequest = tblMcRequest;
	}

}