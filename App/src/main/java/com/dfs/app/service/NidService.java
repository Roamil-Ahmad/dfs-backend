package com.dfs.app.service;

import com.dfs.app.dto.NidBvsRequest;
import com.dfs.app.dto.common.Request;

import java.math.BigDecimal;
import java.util.HashMap;

public interface NidService {
    HashMap<String, Object> bioversys(NidBvsRequest nidBvsRequest, Request request, BigDecimal userId);
}
