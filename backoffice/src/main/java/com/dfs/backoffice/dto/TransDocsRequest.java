package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransDocsRequest {
    private long transDocsId;
    private String isActive;
    private String limitYn;
    private String transDocsCode;
    private String transDocsDescr;
    private BigDecimal glAccountId;
    private BigDecimal taxRegimeId;
    private BigDecimal dailyAmtLimitCr;
    private BigDecimal dailyAmtLimitDr;
    private BigDecimal monthlyAmtLimitCr;
    private BigDecimal monthlyAmtLimitDr;
}
