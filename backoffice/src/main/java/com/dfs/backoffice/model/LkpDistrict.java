package com.dfs.backoffice.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="LKP_DISTRICT")
@Data
public class LkpDistrict implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LKP_DISTRICT_DISTRICTID_GENERATOR", sequenceName="LKP_DISTRICT_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LKP_DISTRICT_DISTRICTID_GENERATOR")
	@Column(name="DISTRICT_ID")
	private long districtId;

	@Column(name="DISTRICT_CODE", nullable=false, unique=true)
	private String districtCode;

	@Column(name="DISTRICT_DESCR")
	private String districtDescr;

	@Column(name="IS_ACTIVE")
	private String isActive;

	@Column(name="PROVINCE_ID")
	private Long provinceId;

	@Column(name="RISK_PROFILE")
	private String riskProfile;

	@Column(name="CREATEUSER")
	private BigDecimal createUser;

	@Column(name="CREATEDATE")
	private Date createDate;

	@Column(name="LASTUPDATEUSER")
	private BigDecimal lastUpdateUser;

	@Column(name="LASTUPDATEDATE")
	private Date lastUpdateDate;

	@Column(name="UPDATEINDEX")
	private BigDecimal updateIndex;

	@Column(name="STATUS_ID")
	private BigDecimal statusId;

	@PreUpdate
	public void incrementUpdateIndex() {
		if (this.updateIndex == null) {
			this.updateIndex = BigDecimal.ONE;
		} else {
			this.updateIndex = this.updateIndex.add(BigDecimal.ONE);
		}
	}

}