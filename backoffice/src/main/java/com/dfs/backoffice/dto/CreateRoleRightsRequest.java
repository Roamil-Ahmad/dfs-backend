package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateRoleRightsRequest {
    private long roleRightsId;
    private String menuId;
    private String roleId;
    private String delete;
    private String view;
    private String insert;
    private String update;
    private String isActive;
    private BigDecimal statusId;

}
