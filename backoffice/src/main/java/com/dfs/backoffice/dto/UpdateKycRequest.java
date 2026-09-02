package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateKycRequest {
    private Long statusId;
    private Long accountId;
    private Long accountLevelId;
    private Long accountUpgradeId;
    private String comments;
}
