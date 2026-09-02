package com.wallet.transaction.service;

import com.wallet.transaction.dto.common.Request;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface CommonService {

    String getResponseMessageByCode(String code);
    HashMap<String, Object> getResponse(String responseCode, Object payload);
    HashMap<String, Object> getResponseWithOutDB(String responseCode,String description, Object payload);
    BigDecimal authenticateHeaderAndDevice(HttpServletRequest httpServletRequest, Request request);
    HashMap<String, Object> getPurposeOfPayment();

    HashMap<String, Object> getAllBank();

    HashMap<String, Object> getAllCurrency();

    HashMap<String, Object> getLovs(List<String> json);
}
