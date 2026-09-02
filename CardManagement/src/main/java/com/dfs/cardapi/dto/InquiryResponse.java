package com.dfs.cardapi.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryResponse {
    private int responseCode;
    private String responseMessage;
    private Object responseBody;
}