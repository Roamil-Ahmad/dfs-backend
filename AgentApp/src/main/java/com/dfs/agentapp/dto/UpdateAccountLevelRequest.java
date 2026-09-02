package com.dfs.agentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAccountLevelRequest {
    private String mobileNumber;
    private String accountLevelCode;
    private String provinceCode;
    private String occupationCode;
    private String sourceOfIncomeCode;
    private String purposeOfAccountCode;
    private String email;
    private List<Document> documents;

}
