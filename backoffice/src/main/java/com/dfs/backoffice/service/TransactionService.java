package com.dfs.backoffice.service;

import com.dfs.backoffice.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TransactionService {

    Response searchTransaction(SearchTransactionRequest searchTransactionRequest, HttpServletRequest request);

    Response transactionDetail(TransactionDetailRequest transactionDetailRequest, HttpServletRequest request);

    Response agentTracking(AgentTrackingRequest agentTrackingRequest, HttpServletRequest request);

    List<TransactionDashboardOne> transactionDashboardOne();

    List<TransactionDashboardTwo> getTransactionDashboardTwo();

    List<CustomerDashboardOne> getCustomerDashboardOne();

    List<CustomerDashboardTwo> getCustomerDashboardTwo();

    List<AgentDashboardOne> getAgentDashboardOne();

    List<AgentDashboardTwo> getAgentDashboardTwo();

    List<IndividualDashboardOne> getIndividualDashboardOne();

    List<IndividualDashboardTwo> getIndividualDashboardTwo();
}
