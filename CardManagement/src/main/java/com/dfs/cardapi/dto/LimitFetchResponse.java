package com.dfs.cardapi.dto;

import lombok.Data;

@Data
public class LimitFetchResponse {
    private Integer responseCode;
    private String responseMessage;
    private LimitFetchResponseBody responseBody;
}
