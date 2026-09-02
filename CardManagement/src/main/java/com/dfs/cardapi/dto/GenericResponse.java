package com.dfs.cardapi.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse {
    private int responseCode;
    private String responseMessage;
    private String error;
}