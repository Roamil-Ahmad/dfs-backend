package com.dfs.cardapi.dto;

import java.util.List;

import lombok.Data;

@Data
public class SpendingSummaryResponse {
    private Integer responseCode;
    private String responseMessage;
    private List<SpendingSummaryResponseBody> responseBody;
}
