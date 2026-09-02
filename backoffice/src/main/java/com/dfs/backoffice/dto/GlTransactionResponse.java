package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlTransactionResponse {
    private long responseStatus;
    private String responseCode;
    private String responseDescr;

}
