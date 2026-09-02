package com.dfs.agentapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_BIOVERISYS database table.
 * 
 */
@Entity
@Table(name="TBL_BIOVERISYS")
@NamedQuery(name="TblBioverisy.findAll", query="SELECT t FROM TblBioverisy t")
public class TblBioverisy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_BIOVERISYS_BIOVERISYSID_GENERATOR", sequenceName="TBL_BIOVERISYS_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_BIOVERISYS_BIOVERISYSID_GENERATOR")
	@Column(name="BIOVERISYS_ID")
	private long bioverisysId;

	@Lob
	@Column(name="BMP_BASE64")
	private String bmpBase64;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="FINGER_INDEX")
	private BigDecimal fingerIndex;

	@Column(name="IS_ACTIVE")
	private String isActive;

	@Lob
	@Column(name="ISO_TEMPLATE_BASE64")
	private String isoTemplateBase64;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="NID_NO")
	private String nidNo;

	@Lob
	@Column(name="TEMPLATE_BASE64")
	private String templateBase64;

	private BigDecimal updateindex;

	@ManyToOne
	@JoinColumn(name="AGENT_ID")
	private TblAgent tblAgent;

	public TblBioverisy() {
	}

	public long getBioverisysId() {
		return this.bioverisysId;
	}

	public void setBioverisysId(long bioverisysId) {
		this.bioverisysId = bioverisysId;
	}

	public String getBmpBase64() {
		return this.bmpBase64;
	}

	public void setBmpBase64(String bmpBase64) {
		this.bmpBase64 = bmpBase64;
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

	public BigDecimal getFingerIndex() {
		return this.fingerIndex;
	}

	public void setFingerIndex(BigDecimal fingerIndex) {
		this.fingerIndex = fingerIndex;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsoTemplateBase64() {
		return this.isoTemplateBase64;
	}

	public void setIsoTemplateBase64(String isoTemplateBase64) {
		this.isoTemplateBase64 = isoTemplateBase64;
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

	public String getNidNo() {
		return this.nidNo;
	}

	public void setNidNo(String nidNo) {
		this.nidNo = nidNo;
	}

	public String getTemplateBase64() {
		return this.templateBase64;
	}

	public void setTemplateBase64(String templateBase64) {
		this.templateBase64 = templateBase64;
	}

	public BigDecimal getUpdateindex() {
		return this.updateindex;
	}

	public void setUpdateindex(BigDecimal updateindex) {
		this.updateindex = updateindex;
	}

	public TblAgent getTblAgent() {
		return tblAgent;
	}

	public void setTblAgent(TblAgent tblAgent) {
		this.tblAgent = tblAgent;
	}
}