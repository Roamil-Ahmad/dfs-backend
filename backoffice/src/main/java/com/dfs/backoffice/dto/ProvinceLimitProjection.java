package com.dfs.backoffice.dto;


import java.math.BigDecimal;
import java.util.Date;

public interface ProvinceLimitProjection {

  Long getProvinceLimitId();

  BigDecimal getProvinceId();
  String getProvinceCode();
  String getProvinceDescr();
  String getProvinceActive();

  BigDecimal getAccountLevelId();
  String getAccountLevelCode();
  String getAccountLevelDescr();
  String getAccountLevelActive();
  BigDecimal getStatusId();

  String getRiskProfile();

  Date getCreatedate();
  BigDecimal getCreateuser();

  Date getLastupdatedate();
  BigDecimal getLastupdateuser();

  BigDecimal getDailyAmtLimitCr();
  BigDecimal getDailyAmtLimitDr();
  BigDecimal getDailyTransLimitCr();
  BigDecimal getDailyTransLimitDr();

  BigDecimal getMonthlyAmtLimitCr();
  BigDecimal getMonthlyAmtLimitDr();
  BigDecimal getMonthlyTransLimitCr();
  BigDecimal getMonthlyTransLimitDr();

  BigDecimal getYearlyAmtLimitCr();
  BigDecimal getYearlyAmtLimitDr();
  BigDecimal getYearlyTransLimitCr();
  BigDecimal getYearlyTransLimitDr();
}
