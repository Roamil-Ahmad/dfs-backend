package com.dfs.agentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BeneficaryResponse {

    private String beneficaryAccountNo;
    private String beneficaryNickName;
    private String beneficaryType;//PRT,PTU,IBFT,BP
    private String beneficaryAccountType;//W,C,B
    private String beneficaryBankImd;
    private String beneficaryMobileNumber;
    private String beneficaryEmail;
    private String beneficaryAccountTitle;
    private long beneficaryImdId;
    private long beneficaryId;
    private String isActive;
}
