package com.dfs.cardapi.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequest {
    private String pan;
    private String statusCode;
}