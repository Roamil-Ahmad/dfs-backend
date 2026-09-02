package com.dfs.backoffice.service;

import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.dto.SearchAgentResponse;
import com.dfs.backoffice.dto.SearchCustomerRequest;
import com.dfs.backoffice.dto.UpdateCustomerRequest;
import com.dfs.backoffice.dto.AgentDetailRequest;
import com.dfs.backoffice.dto.AgentDetailResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AgentMaintenanceService {
  List<SearchAgentResponse> searchAgents(SearchCustomerRequest searchCustomerRequest);

  Response updateAgent(UpdateCustomerRequest updateCustomerRequest, HttpServletRequest request);

  AgentDetailResponse getAgentDetail(AgentDetailRequest request);
}
