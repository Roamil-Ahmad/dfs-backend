package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearch {
    private String employeeName;
    private String email;
    private String mobileNo;
    private String statusId;
    private String roleId;
    private String employeeNo;
    private String fromDate;
    private String toDate;
}
