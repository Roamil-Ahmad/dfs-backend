package com.dfs.cardapi.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LimitValidateResponseBody {
    private double availableLimit;
    private int availableTranCount;
}
