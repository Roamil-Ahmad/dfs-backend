package com.dfs.agentapp.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewRequest {
    private String productCode;
    private String relationshipNumber;
    private String accountNumber;
    private String cardTitle;
    private int cardType;
    private int requestTypeId;
}