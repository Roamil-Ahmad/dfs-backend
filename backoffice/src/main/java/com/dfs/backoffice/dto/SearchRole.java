package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRole {
    private String roleDescr;
    private String statusId;
    private String createUser;
    private String fromDate;
    private String toDate;
}
