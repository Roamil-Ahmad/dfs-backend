package com.mfs.pricingprofile.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_TRANS_CHARGES database table.
 * 
 */
@Entity
@Table(name="TBL_TRANS_CHARGES")
@NamedQuery(name="TblTransCharge.findAll", query="SELECT t FROM TblTransCharge t")
public class TblTransCharge implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TBL_TRANS_CHARGES_TRANSCHARGESID_GENERATOR", sequenceName="TBL_TRANS_CHARGES_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_TRANS_CHARGES_TRANSCHARGESID_GENERATOR")
	@Column(name="TRANS_CHARGES_ID")
	private long transChargesId;

	@Column(name="CHARGES_INCL_EXCL")
	private String chargesInclExcl;

	@Column(name="CHARGES_PROFILE_NAME")
	private String chargesProfileName;

	private Date createdate;

	private BigDecimal createuser;

	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_FROM")
	private Date effectiveFrom;

	@Temporal(TemporalType.DATE)
	@Column(name="EFFECTIVE_TO")
	private Date effectiveTo;

	@Column(name="FED_INCL_EXCL")
	private String fedInclExcl;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	@Column(name="VELOCITY_AMOUNT")
	private BigDecimal velocityAmount;

	@Column(name="VELOCITY_FREQUENCY")
	private String velocityFrequency;

	@Column(name="VELOCITY_COUNT")
	private String velocityCount;

	//bi-directional many-to-one association to LkpStatus
	@ManyToOne
	@JoinColumn(name="STATUS_ID")
	private LkpStatus lkpStatus;

	//bi-directional many-to-one association to TblGlAccount
	@ManyToOne
	@JoinColumn(name="GL_ACCOUNT_ID")
	private TblGlAccount tblGlAccount;

	//bi-directional many-to-one association to TblTransChargesChannel
	@OneToMany(mappedBy="tblTransCharge", cascade = CascadeType.ALL)
	private List<TblTransChargesChannel> tblTransChargesChannels;

	//bi-directional many-to-one association to TblTransChargesDoc
	@OneToMany(mappedBy="tblTransCharge", cascade = CascadeType.ALL)
	private List<TblTransChargesDoc> tblTransChargesDocs;

	//bi-directional many-to-one association to TblTransChargesSegment
	@OneToMany(mappedBy="tblTransCharge", cascade = CascadeType.ALL)
	private List<TblTransChargesSegment> tblTransChargesSegments;

	//bi-directional many-to-one association to TblTransChargesSlab
	@OneToMany(mappedBy="tblTransCharge", cascade = CascadeType.ALL)
	private List<TblTransChargesSlab> tblTransChargesSlabs;

	public long getTransChargesId() {
		return this.transChargesId;
	}

	public void setTransChargesId(long transChargesId) {
		this.transChargesId = transChargesId;
	}

	public String getChargesInclExcl() {
		return this.chargesInclExcl;
	}

	public void setChargesInclExcl(String chargesInclExcl) {
		this.chargesInclExcl = chargesInclExcl;
	}

	public String getChargesProfileName() {
		return this.chargesProfileName;
	}

	public void setChargesProfileName(String chargesProfileName) {
		this.chargesProfileName = chargesProfileName;
	}

	public Date getCreatedate() {
		return createdate;
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

	public Date getEffectiveFrom() {
		return this.effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Date getEffectiveTo() {
		return this.effectiveTo;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public String getFedInclExcl() {
		return this.fedInclExcl;
	}

	public void setFedInclExcl(String fedInclExcl) {
		this.fedInclExcl = fedInclExcl;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Date getLastupdatedate() {
		return lastupdatedate;
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

	public BigDecimal getVelocityAmount() {
		return this.velocityAmount;
	}

	public void setVelocityAmount(BigDecimal velocityAmount) {
		this.velocityAmount = velocityAmount;
	}

	public String getVelocityFrequency() {
		return this.velocityFrequency;
	}

	public void setVelocityFrequency(String velocityFrequency) {
		this.velocityFrequency = velocityFrequency;
	}

	public LkpStatus getLkpStatus() {
		return this.lkpStatus;
	}

	public void setLkpStatus(LkpStatus lkpStatus) {
		this.lkpStatus = lkpStatus;
	}

	public TblGlAccount getTblGlAccount() {
		return this.tblGlAccount;
	}

	public void setTblGlAccount(TblGlAccount tblGlAccount) {
		this.tblGlAccount = tblGlAccount;
	}

	public List<TblTransChargesChannel> getTblTransChargesChannels() {
		return this.tblTransChargesChannels;
	}

	public void setTblTransChargesChannels(List<TblTransChargesChannel> tblTransChargesChannels) {
		this.tblTransChargesChannels = tblTransChargesChannels;
	}

	public TblTransChargesChannel addTblTransChargesChannel(TblTransChargesChannel tblTransChargesChannel) {
		getTblTransChargesChannels().add(tblTransChargesChannel);
		tblTransChargesChannel.setTblTransCharge(this);

		return tblTransChargesChannel;
	}

	public TblTransChargesChannel removeTblTransChargesChannel(TblTransChargesChannel tblTransChargesChannel) {
		getTblTransChargesChannels().remove(tblTransChargesChannel);
		tblTransChargesChannel.setTblTransCharge(null);

		return tblTransChargesChannel;
	}

	public List<TblTransChargesDoc> getTblTransChargesDocs() {
		return this.tblTransChargesDocs;
	}

	public void setTblTransChargesDocs(List<TblTransChargesDoc> tblTransChargesDocs) {
		this.tblTransChargesDocs = tblTransChargesDocs;
	}

	public TblTransChargesDoc addTblTransChargesDoc(TblTransChargesDoc tblTransChargesDoc) {
		getTblTransChargesDocs().add(tblTransChargesDoc);
		tblTransChargesDoc.setTblTransCharge(this);

		return tblTransChargesDoc;
	}

	public TblTransChargesDoc removeTblTransChargesDoc(TblTransChargesDoc tblTransChargesDoc) {
		getTblTransChargesDocs().remove(tblTransChargesDoc);
		tblTransChargesDoc.setTblTransCharge(null);

		return tblTransChargesDoc;
	}

	public List<TblTransChargesSegment> getTblTransChargesSegments() {
		return this.tblTransChargesSegments;
	}

	public void setTblTransChargesSegments(List<TblTransChargesSegment> tblTransChargesSegments) {
		this.tblTransChargesSegments = tblTransChargesSegments;
	}

	public TblTransChargesSegment addTblTransChargesSegment(TblTransChargesSegment tblTransChargesSegment) {
		getTblTransChargesSegments().add(tblTransChargesSegment);
		tblTransChargesSegment.setTblTransCharge(this);

		return tblTransChargesSegment;
	}

	public TblTransChargesSegment removeTblTransChargesSegment(TblTransChargesSegment tblTransChargesSegment) {
		getTblTransChargesSegments().remove(tblTransChargesSegment);
		tblTransChargesSegment.setTblTransCharge(null);

		return tblTransChargesSegment;
	}

	public List<TblTransChargesSlab> getTblTransChargesSlabs() {
		return this.tblTransChargesSlabs;
	}

	public void setTblTransChargesSlabs(List<TblTransChargesSlab> tblTransChargesSlabs) {
		this.tblTransChargesSlabs = tblTransChargesSlabs;
	}

	public TblTransChargesSlab addTblTransChargesSlab(TblTransChargesSlab tblTransChargesSlab) {
		getTblTransChargesSlabs().add(tblTransChargesSlab);
		tblTransChargesSlab.setTblTransCharge(this);

		return tblTransChargesSlab;
	}

	public TblTransChargesSlab removeTblTransChargesSlab(TblTransChargesSlab tblTransChargesSlab) {
		getTblTransChargesSlabs().remove(tblTransChargesSlab);
		tblTransChargesSlab.setTblTransCharge(null);

		return tblTransChargesSlab;
	}

	public String getVelocityCount() {
		return velocityCount;
	}

	public void setVelocityCount(String velocityCount) {
		this.velocityCount = velocityCount;
	}
}