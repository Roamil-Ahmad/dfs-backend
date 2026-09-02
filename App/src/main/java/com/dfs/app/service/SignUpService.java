package com.dfs.app.service;

import com.dfs.app.dto.MobileRegistrationRequest;
import com.dfs.app.dto.common.CustomerKycRequest;
import com.dfs.app.dto.common.Request;
import com.dfs.app.model.TblCustomerAll;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;

public interface SignUpService {
    TblCustomerAll registerCustomerAll(MobileRegistrationRequest mobileRegistrationRequest, Request apiRequest);

    TblCustomerAll updateTblCustomerAllVerifed(String mobileNumber,Request request);

    HashMap<String, Object> registerCustomerAndAccount(CustomerKycRequest customerKycRequest, Request apiRequest, String header) throws JsonProcessingException;

    HashMap<String, Object> getdfsid(long parseLong);
}
