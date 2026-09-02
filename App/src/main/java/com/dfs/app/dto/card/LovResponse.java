package com.dfs.app.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LovResponse {
    private int responseCode;
    private String responseMessage;
    private Object responseBody;
}