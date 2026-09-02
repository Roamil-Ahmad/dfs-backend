package com.dfs.app.service;

import com.dfs.app.dto.AddFavPayRequest;
import com.dfs.app.dto.GetBalanceRequest;
import com.dfs.app.dto.UpdateFavPayRequest;
import com.dfs.app.dto.common.Request;

import java.math.BigDecimal;
import java.util.HashMap;

public interface FavPayService {
    HashMap<String, Object> saveBeneficary(AddFavPayRequest favPayRequest, BigDecimal userId, Request request);

    HashMap<String, Object> getAllFavpay(GetBalanceRequest getBalanceRequest, Request request);

    HashMap<String, Object> updateFavPay(UpdateFavPayRequest updateFavPayRequest, BigDecimal userId, Request request);
}
