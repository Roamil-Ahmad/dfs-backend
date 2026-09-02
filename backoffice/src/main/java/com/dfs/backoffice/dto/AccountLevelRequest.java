package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountLevelRequest {
	private long accountLevelId;
	private String accountLevelCode;
	private String accountLevelDescr;
	private BigDecimal dailyAmtLimitCr;
	private BigDecimal dailyAmtLimitDr;
	private BigDecimal dailyTransLimitCr;
	private BigDecimal dailyTransLimitDr;
	private BigDecimal maxAmtLimit;
	private BigDecimal maxAmtPerTxn;
	private BigDecimal monthlyAmtLimitCr;
	private BigDecimal monthlyAmtLimitDr;
	private BigDecimal monthlyTransLimitCr;
	private BigDecimal monthlyTransLimitDr;
	private String isActive;
	private BigDecimal yearlyAmtLimitCr;
	private BigDecimal yearlyAmtLimitDr;
	private BigDecimal yearlyTransLimitCr;
	private BigDecimal yearlyTransLimitDr;
	private BigDecimal glAccountId;
}