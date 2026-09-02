package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
public class TblTransLimitRequest {

	private long transLimitId;
	private BigDecimal dailyAmtLimitCr;
	private BigDecimal dailyAmtLimitDr;
	private BigDecimal dailyTransLimitCr;
	private BigDecimal dailyTransLimitDr;
	private String excludeLimit;
	private String isActive;
	private String limitProfileName;
	private BigDecimal monthlyAmtLimitCr;
	private BigDecimal monthlyAmtLimitDr;
	private BigDecimal monthlyTransLimitCr;
	private BigDecimal monthlyTransLimitDr;
	private BigDecimal yearlyAmtLimitCr;
	private BigDecimal yearlyAmtLimitDr;
	private BigDecimal yearlyTransLimitCr;
	private BigDecimal yearlyTransLimitDr;
	private List<TblTransLimitDetailRequest> tblTransLimitDetail;

}