package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InviteStatsResponse {
    private int totalInvites;
    private int pendingInvites;
    private int completeInvites;
}
