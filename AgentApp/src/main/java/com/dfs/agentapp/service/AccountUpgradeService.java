package com.dfs.agentapp.service;

import com.dfs.agentapp.dto.UpdateAccountLevelRequest;
import com.dfs.agentapp.dto.common.Request;

import java.math.BigDecimal;
import java.util.HashMap;

public interface AccountUpgradeService {
    HashMap<String, Object> upgradeToL2(UpdateAccountLevelRequest updateAccountLevelRequest, Request request, BigDecimal userId);
}
