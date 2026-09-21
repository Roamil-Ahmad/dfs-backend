package com.dfs.agentapp.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryResponse {
    private int responseCode;
    private String responseMessage;
    private Object responseBody;
}