package com.dfs.app.service;

import com.dfs.app.dto.UpdateAccountLevelRequest;
import com.dfs.app.dto.common.Request;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;
import java.util.HashMap;

public interface AccountUpgradeService {
    HashMap<String, Object> upgradeToL2(UpdateAccountLevelRequest updateAccountLevelRequest, Request request, BigDecimal userId, String header) throws JsonProcessingException;

    boolean checkExistance(String mobileNumber, String accountLevelCode);

    String getSelfieImage(String mobileNumber);
}
