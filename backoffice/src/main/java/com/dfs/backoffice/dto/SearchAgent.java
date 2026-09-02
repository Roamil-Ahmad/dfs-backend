package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchAgent {
    private String cnic;
    private String mobileNo;
    private String agentClassId;
    private String fromDate;
    private String toDate;
}
