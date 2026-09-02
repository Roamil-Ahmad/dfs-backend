package com.dfs.cardapi.dto;

import lombok.*;

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