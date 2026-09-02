package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CreateAgentAccountRequest {
    private long agentid;
    private String parentAgentid;
    private String nameOfApplicant;
    private String applicantMobileNo;
    private String applicantNidNo;
    private String applicantTelephoneNo;
    private String fatherHusbandName;
    private String residentialAddress;
    private String provinceId;
    private String districtId;
    private String cityId;
    private String email;
    private String nok;
    private String agentName;
    private String occupation;
    private String agentAddress;
    private String agentNidNo;
    private Date nidIssueDate;
    private Date nidExpiryDate;
    private String agentMobileNo;
    private String agentTelephoneNo;
    private String isActive;
    private String agentClassId;
    private List<AgentCommissionDistributions> tblAgentCommissionDistributions;
    private String agentLevel;
}
