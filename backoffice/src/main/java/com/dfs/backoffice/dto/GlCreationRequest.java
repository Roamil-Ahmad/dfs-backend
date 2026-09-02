package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GlCreationRequest {
    private long glAccountId;
    private String glAccountCode;
    private String glAccountDescr;
    private String isActive;
    private String overdrawn;
    private BigDecimal parRefId;
    private BigDecimal accountTypeId;
}
