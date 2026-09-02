package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateMenuRequest {
    private long menuId;
    private String menuCode;
    private String menuDescr;
    private String menuType;
    private BigDecimal parentMenu;
    private String sortSeq;
    private String menuPath;
    private String icon;
    private String isActive;
    private BigDecimal statusId;

}
