package com.dfs.backoffice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TblDistrictLimitSearchRequest {
  private BigDecimal districtId;         // Optional filter
  private BigDecimal accountLevelId;     // Optional filter
  private String riskProfile;            // Optional filter
  private Date dateFrom;                 // Optional filter
  private Date dateTo;                   // Optional filter
}
