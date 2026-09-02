package com.dfs.backoffice.service;

import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.TblCustomer;
import com.dfs.backoffice.model.VwMiniStatement;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CustomerMaintenanceService {
    List<SearchCustomerResponse> searchCustomer(SearchCustomerRequest searchCustomerRequest);

    List<VwMiniStatement> getLastTenTransactions(MiniStatementRequest miniStatementRequest, HttpServletRequest request);

    Response updateCustomer(UpdateCustomerRequest updateCustomerRequest, HttpServletRequest request);

    TblCustomer getCustomerByNidNo(String nidNo);

    Response updateAccountStatus(UpdateStatusRequest updateStatusRequest, HttpServletRequest request);

    List<VwMiniStatement> getCardLastTransactions(MiniStatementRequest miniStatementRequest, HttpServletRequest request);
}
