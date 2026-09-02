package com.dfs.backoffice.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the TBL_AGENT_CLASS_CHARGES database table.
 */
@Entity
@Table(name = "TBL_AGENT_CLASS_CHARGES")
@NamedQuery(name = "TblAgentClassCharge.findAll", query = "SELECT t FROM TblAgentClassCharge t")
public class TblAgentClassCharge implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TBL_AGENT_CLASS_CHARGES_AGENTCLASSCHARGESID_GENERATOR", sequenceName = "TBL_AGENT_CLASS_CHARGES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBL_AGENT_CLASS_CHARGES_AGENTCLASSCHARGESID_GENERATOR")
    @Column(name = "AGENT_CLASS_CHARGES_ID")
    private long agentClassChargesId;

    private Timestamp createdate;

    private BigDecimal createuser;

    @Column(name = "IS_ACTIVE")
    private String isActive;

    private Timestamp lastupdatedate;

    private BigDecimal lastupdateuser;

    private BigDecimal updateindex;

    //bi-directional many-to-one association to TblAgentClass
    @ManyToOne
    @JoinColumn(name = "AGENT_CLASS_ID")
    private TblAgentClass tblAgentClass;

    //bi-directional many-to-one association to TblTransCharge
    @ManyToOne
    @JoinColumn(name = "TRANS_CHARGES_ID")
    private TblTransCharge tblTransCharge;

    public TblAgentClassCharge() {
    }

    public long getAgentClassChargesId() {
        return this.agentClassChargesId;
    }

    public void setAgentClassChargesId(long agentClassChargesId) {
        this.agentClassChargesId = agentClassChargesId;
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

    public String getIsActive() {
        return this.isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Timestamp getLastupdatedate() {
        return this.lastupdatedate;
    }

    public void setLastupdatedate(Timestamp lastupdatedate) {
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

    public TblAgentClass getTblAgentClass() {
        return this.tblAgentClass;
    }

    public void setTblAgentClass(TblAgentClass tblAgentClass) {
        this.tblAgentClass = tblAgentClass;
    }

    public TblTransCharge getTblTransCharge() {
        return this.tblTransCharge;
    }

    public void setTblTransCharge(TblTransCharge tblTransCharge) {
        this.tblTransCharge = tblTransCharge;
    }

}