package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuditLogResponse {
    private Date activityDate;
    private String mobileNumber;
    private String userName;
    private String activityDetail;
    private String deviceModel;
    private String ipAddress;
}
