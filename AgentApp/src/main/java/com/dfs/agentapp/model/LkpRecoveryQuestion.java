package com.dfs.agentapp.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the LKP_RECOVERY_QUESTION database table.
 */
@Entity
@Table(name="LKP_RECOVERY_QUESTION")
@NamedQuery(name="LkpRecoveryQuestion.findAll", query="SELECT l FROM LkpRecoveryQuestion l")
public class LkpRecoveryQuestion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="RECOVERY_QUESTION_ID")
	private long recoveryQuestionId;

	@Column(name="RECOVERY_QUESTION_CODE")
	private String recoveryQuestionCode;

	@Column(name="RECOVERY_QUESTION_DESCR")
	private String recoveryQuestionDescr;

	@Column(name="RECOVERY_QUESTION_NAME")
	private String recoveryQuestionName;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date createdate;

	private BigDecimal createuser;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	private BigDecimal updateindex;

	public LkpRecoveryQuestion() {
	}

	public long getRecoveryQuestionId() { return this.recoveryQuestionId; }
	public void setRecoveryQuestionId(long recoveryQuestionId) { this.recoveryQuestionId = recoveryQuestionId; }

	public String getRecoveryQuestionCode() { return this.recoveryQuestionCode; }
	public void setRecoveryQuestionCode(String recoveryQuestionCode) { this.recoveryQuestionCode = recoveryQuestionCode; }

	public String getRecoveryQuestionDescr() { return this.recoveryQuestionDescr; }
	public void setRecoveryQuestionDescr(String recoveryQuestionDescr) { this.recoveryQuestionDescr = recoveryQuestionDescr; }

	public String getRecoveryQuestionName() { return this.recoveryQuestionName; }
	public void setRecoveryQuestionName(String recoveryQuestionName) { this.recoveryQuestionName = recoveryQuestionName; }

	public String getIsActive() { return this.isActive; }
	public void setIsActive(String isActive) { this.isActive = isActive; }

	public Date getCreatedate() { return this.createdate; }
	public void setCreatedate(Date createdate) { this.createdate = createdate; }

	public BigDecimal getCreateuser() { return this.createuser; }
	public void setCreateuser(BigDecimal createuser) { this.createuser = createuser; }

	public Date getLastupdatedate() { return this.lastupdatedate; }
	public void setLastupdatedate(Date lastupdatedate) { this.lastupdatedate = lastupdatedate; }

	public BigDecimal getLastupdateuser() { return this.lastupdateuser; }
	public void setLastupdateuser(BigDecimal lastupdateuser) { this.lastupdateuser = lastupdateuser; }

	public BigDecimal getUpdateindex() { return this.updateindex; }
	public void setUpdateindex(BigDecimal updateindex) { this.updateindex = updateindex; }
}
