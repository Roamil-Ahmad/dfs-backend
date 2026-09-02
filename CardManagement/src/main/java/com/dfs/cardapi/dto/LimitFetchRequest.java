package com.dfs.cardapi.dto;

import lombok.Data;

@Data
public class LimitFetchRequest {
    private String pan;
    private Integer channelCode;
    private Integer tranCode;
}
