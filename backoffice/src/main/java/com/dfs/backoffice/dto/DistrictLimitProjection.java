package com.dfs.backoffice.dto;

import java.math.BigDecimal;
import java.util.Date;

public interface DistrictLimitProjection {

  Long getDistrictLimitId();

  // District info
  BigDecimal getDistrictId();
  String getDistrictCode();
  String getDistrictDescr();
  String getDistrictActive();

  // Account Level info
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