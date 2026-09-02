package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeCardStatusRequest {
    private String pan;
    private String statusCode;
}
