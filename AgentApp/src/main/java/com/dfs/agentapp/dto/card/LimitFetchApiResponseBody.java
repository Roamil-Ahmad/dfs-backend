package com.dfs.agentapp.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LimitFetchApiResponseBody {
    private Double availableLimit;
    private Double maxLimit;
    private Long availableTranCount;
}
