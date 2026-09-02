package com.dfs.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
public class DistrictWithProvince {

  private Long districtId; // use Long instead of long
  private String districtCode;
  private String districtDescr;
  private String isActive;
  private Long provinceId;
  private String riskProfile;
  private BigDecimal createUser;
  private Date createDate;  // make sure query returns java.util.Date
  private BigDecimal lastUpdateUser;
  private Date lastUpdateDate;
  private BigDecimal updateIndex;

  // province description
  private String provinceDescr;

}