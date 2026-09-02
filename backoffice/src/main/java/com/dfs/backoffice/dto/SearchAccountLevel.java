package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchAccountLevel {
    private String accountLevelDescr;
    private String glAccountId;
    private String fromDate;
    private String toDate;
}
