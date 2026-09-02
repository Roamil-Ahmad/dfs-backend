package com.dfs.agentapp.service;

import com.dfs.agentapp.dto.MobileRegistrationRequest;
import com.dfs.agentapp.dto.common.AgentKycRequest;
import com.dfs.agentapp.dto.common.CorporateOnboardingRequest;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.model.TblAgent;
import com.dfs.agentapp.model.TblAppUserLoginHistory;
import com.dfs.agentapp.model.TblCustomerAll;
import com.dfs.agentapp.model.TblDeviceInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;

public interface SignUpService {

    TblAgent checkAgentExistance(String encryptwith256);

    TblDeviceInfo registerDeviceInfo(MobileRegistrationRequest mobileRegistrationRequest, TblAgent tblAgent);

    TblAppUserLoginHistory saveLoginHistory(long agentId);

    TblAgent updateTblAgentAllVerifed(String mobileNumber);

    TblCustomerAll registerCustomerAll(MobileRegistrationRequest mobileRegistrationRequest, Request apiRequest);

    TblCustomerAll updateTblCustomerAllVerifed(String mobileNumber, Request apiRequest);

    HashMap<String, Object> registerAgentAndAccount(AgentKycRequest agentKycRequest, Request apiRequest, String header) throws JsonProcessingException;

    /**
     * Same as {@link #registerAgentAndAccount} but without the maker-checker step: the agent is
     * saved and returned outright, never parked for approval.
     */
    HashMap<String, Object> registerAgentAndAccountWithoutMakerChecker(AgentKycRequest agentKycRequest, Request apiRequest) throws JsonProcessingException;

    /**
     * Onboards an agent in one call: device registration, verification and KYC together, with the
     * OTP round trip left out.
     */
    HashMap<String, Object> onboardCorporateAgent(CorporateOnboardingRequest request, Request apiRequest) throws JsonProcessingException;

    TblAgent saveAgentData(AgentKycRequest agentKycRequest, Request apiRequest);
}
