package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateRoleRequest {
    private long roleId;
    private String roleCode;
    private String roleDescr;
    private String isActive;
    private BigDecimal statusId;

}
