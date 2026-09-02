package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditLogRequest {
    private String mobileNo;
    private String userName;
    private String fromDate;
    private String toDate;
}
