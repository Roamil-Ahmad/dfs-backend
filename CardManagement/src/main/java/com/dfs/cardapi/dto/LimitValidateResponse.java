package com.dfs.cardapi.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LimitValidateResponse {
    private int responseCode;
    private String responseMessage;
    private LimitValidateResponseBody responseBody;
}
