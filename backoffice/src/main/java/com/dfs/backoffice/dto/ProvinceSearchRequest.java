package com.dfs.backoffice.dto;

import lombok.Data;

@Data
public class ProvinceSearchRequest {
  private String provinceCode;
  private String provinceDescr;
  private String dateFrom; // yyyy-MM-dd
  private String dateTo;   // yyyy-MM-dd
}
