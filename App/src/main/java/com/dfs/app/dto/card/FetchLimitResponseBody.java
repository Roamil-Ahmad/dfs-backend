package com.dfs.app.dto.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FetchLimitResponseBody {
    private String accountNumber;
    private String pan;
    private String cardTitle;
    private String expiryDate;
    private String cardStatusName;
    private Double maxLimit;
    private Double singleTranLimit;
    private Double dailyAvailableSpending;
    private Double monthlyAvailableSpending;
}
