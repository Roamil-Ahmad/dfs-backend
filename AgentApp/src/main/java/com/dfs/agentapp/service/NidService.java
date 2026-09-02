package com.dfs.agentapp.service;

import com.dfs.agentapp.dto.NidBvsRequest;
import com.dfs.agentapp.dto.common.Request;

import java.math.BigDecimal;
import java.util.HashMap;

public interface NidService {
    HashMap<String, Object> bioverisys(NidBvsRequest nidBvsRequest, Request request, BigDecimal userId);
}
