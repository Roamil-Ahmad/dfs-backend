package com.dfs.cardapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseBody {
    private String token;
    private String loginId;
    private String fullName;
    private int isActive;
    private String policyId;
    private String emailAddress;
    private String mobileNo;
    private String createdOn;
    private String createdBy;
    private String updatedOn;
    private String updatedBy;
    private String pwdUpdatedOn;
    private int pwdRetryCount;
    private boolean isResetRequired;
    private List<String> groupIds;
}