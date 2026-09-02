package com.dfs.backoffice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProvinceRequest {
  private Long provinceId;
  private String provinceCode;
  private String provinceDescr;
  private String isActive;
  private String capital;
  private String ibanCode;
  private String riskProfile;
  private BigDecimal dailyAmtLimitDr;
  private BigDecimal monthlyAmtLimitDr;
  private BigDecimal yearlyAmtLimitDr;
  private BigDecimal dailyAmtLimitCr;
  private BigDecimal monthlyAmtLimitCr;
  private BigDecimal yearlyAmtLimitCr;
  private BigDecimal dailyTransLimitDr;
  private BigDecimal monthlyTransLimitDr;
  private BigDecimal yearlyTransLimitDr;
  private BigDecimal dailyTransLimitCr;
  private BigDecimal monthlyTransLimitCr;
  private BigDecimal yearlyTransLimitCr;
}
