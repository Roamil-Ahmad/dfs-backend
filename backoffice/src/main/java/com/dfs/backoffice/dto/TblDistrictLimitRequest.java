package com.dfs.backoffice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TblDistrictLimitRequest {
  private long districtLimitId;
  private BigDecimal districtId;
  private BigDecimal accountLevelId;
  private String riskProfile;
  private BigDecimal dailyAmtLimitCr;
  private BigDecimal dailyAmtLimitDr;
  private BigDecimal dailyTransLimitCr;
  private BigDecimal dailyTransLimitDr;
  private BigDecimal monthlyAmtLimitCr;
  private BigDecimal monthlyAmtLimitDr;
  private BigDecimal monthlyTransLimitCr;
  private BigDecimal monthlyTransLimitDr;
  private BigDecimal yearlyAmtLimitCr;
  private BigDecimal yearlyAmtLimitDr;
  private BigDecimal yearlyTransLimitCr;
  private BigDecimal yearlyTransLimitDr;
}