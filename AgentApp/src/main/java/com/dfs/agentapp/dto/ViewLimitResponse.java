package com.dfs.agentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ViewLimitResponse {
    private Long accountId;
    private BigDecimal dailyDrTotal;
    private BigDecimal monthlyDrTotal;
    private BigDecimal yearlyDrTotal;
    private BigDecimal dailyCrTotal;
    private BigDecimal monthlyCrTotal;
    private BigDecimal yearlyCrTotal;
    private BigDecimal dailyDrRemaining;
    private BigDecimal monthlyDrRemaining;
    private BigDecimal yearlyDrRemaining;
    private BigDecimal dailyCrRemaining;
    private BigDecimal monthlyCrRemaining;
    private BigDecimal yearlyCrRemaining;
}
