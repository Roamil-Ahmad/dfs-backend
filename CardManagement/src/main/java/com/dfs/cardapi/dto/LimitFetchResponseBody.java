package com.dfs.cardapi.dto;

import lombok.Data;

@Data
public class LimitFetchResponseBody {
    private Double availableLimit;
    private Double maxLimit;
    private Integer availableTranCount;
}
