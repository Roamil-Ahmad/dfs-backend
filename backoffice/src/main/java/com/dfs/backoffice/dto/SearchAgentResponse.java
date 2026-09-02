package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class SearchAgentResponse {

        private Long agentId;
        private String name;
        private String fatherHusbandName;
        private String grandfatherName;
        private String nidNo;
        private Date dob;
        private String email;
        private String residentialAddress;
        private String permanentAddress;
        private Date createDate;
        private String gender;
        private String isActive;
        private String pob;
        private Date nidExpiryDate;
        private Date nidIssueDate;

        private Long accountId;
        private String accountLevelDescr;
        private String accountStatusDescr;
}