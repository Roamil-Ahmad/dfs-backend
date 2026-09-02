package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchLimitsRequest {
    private String accountId;
    private String pan;
}
