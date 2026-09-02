package com.dfs.cardapi.dto;

import lombok.Data;

@Data
public class SpendingSummaryRequest {
    private String accountNumber;
    private String pan;
    private String channelCode;
    private String tranCode;
}
