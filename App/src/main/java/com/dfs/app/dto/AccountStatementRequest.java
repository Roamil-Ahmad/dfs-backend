package com.dfs.app.dto;

import com.dfs.app.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatementRequest {
    private String appUserId;
    private String fromDate;
    private String toDate;
    private String email;
}
