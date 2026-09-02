package com.dfs.backoffice.service;

import com.dfs.backoffice.dto.AgentApprovalRequest;
import com.dfs.backoffice.dto.CreateAgentAccountRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.dto.SearchAgent;
import com.dfs.backoffice.model.TblAgent;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AgentOnBoardingService {
    Response createAgentAccount(CreateAgentAccountRequest createAgentAccountRequest, HttpServletRequest request);

    Response updateAgentAccount(CreateAgentAccountRequest createAgentAccountRequest, HttpServletRequest request);

    TblAgent getAgentById(String agentId);

    List<TblAgent> getAllAgents(SearchAgent searchAgent);
    Response agentApproval(AgentApprovalRequest agentApprovalRequest, HttpServletRequest request) throws JsonProcessingException;
}
