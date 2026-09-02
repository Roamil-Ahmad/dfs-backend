package com.dfs.backoffice.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the LKP_PROVINCE database table.
 *
 */
@Entity
@Data
@Table(name = "LKP_PROVINCE")
public class LkpProvince {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_PROVINCE_PROVINCEID_GENERATOR", sequenceName="LKP_PROVINCE_SEQ",  allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_PROVINCE_PROVINCEID_GENERATOR")
	@Column(name="PROVINCE_ID")
	private long provinceId;

	private String capital;

	private Date createdate;

	private BigDecimal createuser;

	@Column(name="IBAN_CODE")
	private String ibanCode;

	@Column(name="IS_ACTIVE")
	private String isActive;

	private Date lastupdatedate;

	private BigDecimal lastupdateuser;

	@Column(name="PROVINCE_CODE")
	private String provinceCode;

	@Column(name="PROVINCE_DESCR")
	private String provinceDescr;

	@Column(name="RISK_PROFILE")
	private String riskProfile;

	private BigDecimal updateindex;

	@PreUpdate
	public void incrementUpdateIndex() {
		if (this.updateindex == null) {
			this.updateindex = BigDecimal.ONE;
		} else {
			this.updateindex = this.updateindex.add(BigDecimal.ONE);
		}
	}
}