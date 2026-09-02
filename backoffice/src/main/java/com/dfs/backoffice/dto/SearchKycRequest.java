package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchKycRequest {
    private String statusId;
    private String mobileNo;
    private String accountTitle;
    private String fromDate;
    private String toDate;
}
