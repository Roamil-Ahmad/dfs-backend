package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCustomerRequest {
    private String mobileNo;
    private String nidNo;
    private String fullName;
    private String accountStatusId;
    private String fromDate;
    private String toDate;
}
