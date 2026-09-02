package com.dfs.app.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchLimitResponse {
    private int responseCode;
    private String responseMessage;
    private FetchLimitResponseBody responseBody;
}
