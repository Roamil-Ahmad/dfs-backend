package com.dfs.backoffice.dto;

import lombok.Data;

@Data
public class DistrictSearchRequest {

  private String districtCode;
  private String districtDescr;
  private Long provinceId;
  private String dateFrom; // yyyy-MM-dd
  private String dateTo;   // yyyy-MM-dd
}
