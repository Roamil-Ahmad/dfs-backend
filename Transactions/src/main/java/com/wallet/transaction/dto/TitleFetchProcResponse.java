package com.wallet.transaction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TitleFetchProcResponse {
    private String accountTitle;
    private BigDecimal fee;
    private String availableBalance;
    private String actualBalance;
    private String responseCode;
    private int responseStatus;
    private String responseDescr;
}
