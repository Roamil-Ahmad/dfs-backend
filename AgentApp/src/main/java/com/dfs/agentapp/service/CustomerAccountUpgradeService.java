package com.dfs.agentapp.service;

import com.dfs.agentapp.dto.CustomerUpdateAccountLevelRequest;
import com.dfs.agentapp.dto.common.Request;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;
import java.util.HashMap;

public interface CustomerAccountUpgradeService {
    HashMap<String, Object> upgradeToL2(CustomerUpdateAccountLevelRequest updateAccountLevelRequest, Request request, BigDecimal userId, String header) throws JsonProcessingException;

    boolean checkExistance(String mobileNumber, String accountLevelCode);

    String getSelfieImage(String mobileNumber);
}
