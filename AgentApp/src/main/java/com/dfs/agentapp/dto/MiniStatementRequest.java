package com.dfs.agentapp.dto;

import com.dfs.agentapp.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiniStatementRequest {
    private String mobileNumber;
    private String fromDate = Constants.EMPTY;
    private String toDate = Constants.EMPTY;
    private String accountLevelCode;
}
