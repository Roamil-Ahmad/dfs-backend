package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCustomerRequest {
    private String fullName;
    private String fatherName;
    private String grandFatherName;
    private String gender;
    private String nidNo;
    private String dob;
    private String placeOfBirth;
    private String permanentAddress;
    private String nidExpiryDate;
    private String nidIssueDate;

}
