package com.dfs.agentapp.service;

import com.dfs.agentapp.dto.AddFavPayRequest;
import com.dfs.agentapp.dto.GetBalanceRequest;
import com.dfs.agentapp.dto.UpdateFavPayRequest;
import com.dfs.agentapp.dto.common.Request;

import java.math.BigDecimal;
import java.util.HashMap;

public interface FavPayService {
    HashMap<String, Object> saveBeneficary(AddFavPayRequest favPayRequest, BigDecimal userId, Request request);

    HashMap<String, Object> getAllFavpay(GetBalanceRequest getBalanceRequest, Request request);

    HashMap<String, Object> updateFavPay(UpdateFavPayRequest updateFavPayRequest, BigDecimal userId, Request request);
}
