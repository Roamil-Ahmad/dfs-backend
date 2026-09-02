package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchTransDocs {
    private String transDocsDescr;
    private String isActive;
    private String fromDate;
    private String toDate;

}
