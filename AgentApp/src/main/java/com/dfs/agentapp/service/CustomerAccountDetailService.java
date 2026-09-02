package com.dfs.agentapp.service;

import com.dfs.agentapp.dto.CashInInquiryRequest;
import com.dfs.agentapp.dto.MpinVerificationRequest;
import com.dfs.agentapp.dto.common.Request;

import java.util.HashMap;

public interface CustomerAccountDetailService {
  HashMap<String, Object> accountDetail(MpinVerificationRequest mpinVerificationRequest, Request request);

  /**
   * Confirms the depositor before an agent cash-in and returns the name and date of birth that
   * /v1/internalcashin needs. Only a customer account qualifies.
   */
  HashMap<String, Object> cashInInquiry(CashInInquiryRequest cashInInquiryRequest, Request request);
}
