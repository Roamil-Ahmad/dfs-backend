package com.dfs.cardapi.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LimitValidateRequest {
    private String pan;
    private int channelCode;
    private int tranCode;
    private double amount;
}
