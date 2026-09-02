package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MenuResponse {
    private BigDecimal menuId;
    private BigDecimal reportId;
    private BigDecimal level;
    private String name;
    private BigDecimal parent_id;
    private String bussfunc;
    private String icon;
    private String url;
    private String deleteAllowed;
    private String viewAllowed;
    private String insertAllowed;
    private String updateAllowed;

}
