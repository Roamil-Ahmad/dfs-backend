package com.dfs.agentapp.service;

import com.dfs.agentapp.dto.MobileRegistrationRequest;
import com.dfs.agentapp.dto.common.CustomerKycRequest;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.model.TblCustomerAll;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;
import java.util.HashMap;

public interface CustomerSignUpService {
    TblCustomerAll registerCustomerAll(MobileRegistrationRequest mobileRegistrationRequest, Request apiRequest, BigDecimal userId);

    TblCustomerAll updateTblCustomerAllVerifed(String mobileNumber,Request request, BigDecimal userId);

    HashMap<String, Object> registerCustomerAndAccount(CustomerKycRequest customerKycRequest, Request apiRequest, String header, BigDecimal userId) throws JsonProcessingException;

    HashMap<String, Object> getdfsid(long parseLong);
}
