package com.dfs.agentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LevelLimitResponse {
    private String accountLevelName;
    private String accountLevelCode;
    private String accountLevelLimit;
}
