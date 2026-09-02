package com.dfs.thirdparties.service;

import com.dfs.thirdparties.dto.common.Request;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

public interface CommonService {

    boolean validatePreRegToken(String token);

    boolean validateToken(String token);

    String getResponseMessageByCode(String code);

    HashMap<String, Object> getResponse(String responseCode,Object payload);

    BigDecimal authenticateHeaderAndDevice(HttpServletRequest httpServletRequest, Request request);

    BigDecimal authenticateHeaderAndDeviceLoggedIn(HttpServletRequest httpServletRequest, Request request);
}
