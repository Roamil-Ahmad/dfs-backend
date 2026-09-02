package com.dfs.agentapp.dto;

import com.dfs.agentapp.dto.common.VerifyOtpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerUpdateAccountLevelRequest {
    private String mobileNumber;
    private String accountLevelCode;
    private String occupationCode;
    private String occupationOther;
    private String sourceOfIncomeCode;
    private String sourceOfIncomeOther;
    private String purposeOfAccountCode;
    private String email;
    private List<Document> documents;
    private String step;
    private VerifyOtpRequest verifyOtpRequest;

}
