package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.repo.VwMiniStatementRepo;
import com.dfs.backoffice.service.TransactionService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl extends HelperClass implements TransactionService {

    @Autowired
    private VwMiniStatementRepo vwMiniStatementRepo;

    @Override
    public Response searchTransaction(SearchTransactionRequest searchTransactionRequest, HttpServletRequest request) {
        Response res = new Response();
        List<Object[]> findTransactions = vwMiniStatementRepo.getResult(searchTransactionRequest.getCardNo(), searchTransactionRequest.getCustomerName(), searchTransactionRequest.getDateFrom(), searchTransactionRequest.getDateTo(),
                searchTransactionRequest.getStatus(), searchTransactionRequest.getTransDocsId(), searchTransactionRequest.getTransRefNum());
        List<SearchTransactionResponse> responseList = new ArrayList<>();
        if (findTransactions.size() > 0) {
            for (Object[] row : findTransactions) {
                SearchTransactionResponse response = new SearchTransactionResponse();
                response.setMwRequestId(toBigDecimal(row[0]));
                response.setTransHeadId(toBigDecimal(row[1]));
                response.setErrorResponse(toStringSafe(row[2]));
                response.setResponseDescr(toStringSafe(row[3]));
                response.setStan(toStringSafe(row[4]));
                response.setRrn(toStringSafe(row[5]));
                response.setAmount(toBigDecimal(row[6]));
                response.setTransDate(toDateSafe(row[7]));
                response.setTransactionType(toStringSafe(row[8]));
                response.setStatus(toStringSafe(row[9]));
                response.setFromAccountNo(toStringSafe(row[10]));
                response.setFromAccountTitle(toStringSafe(row[11]));
                response.setToAccountTitle(toStringSafe(row[12]));
                response.setToAccountNo(toStringSafe(row[13]));
                response.setTransRefNum(toBigDecimal(row[14]));
                response.setCardNo(toStringSafe(row[15]));

                responseList.add(response);
            }
            setResponse(res, Constants.ONE, responseList, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(res, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return res;
    }

    @Override
    public Response transactionDetail(TransactionDetailRequest dataDetailRequest, HttpServletRequest request) {
        Response res = new Response();
        List<Object[]> findTransactions = vwMiniStatementRepo.getResultTrsanctionDetail(dataDetailRequest.getMwRequestId());
        SearchTransactionResponse response = new SearchTransactionResponse();
        if (findTransactions.size() > 0) {
            Object[] row = findTransactions.get(0); // Only process the first result
            response.setMwRequestId(toBigDecimal(row[0]));
            response.setTransHeadId(toBigDecimal(row[1]));
            response.setErrorResponse(toStringSafe(row[2]));
            response.setResponseDescr(toStringSafe(row[3]));
            response.setStan(toStringSafe(row[4]));
            response.setRrn(toStringSafe(row[5]));
            response.setAmount(toBigDecimal(row[6]));
            response.setTransDate(toDateSafe(row[7]));
            response.setTransactionType(toStringSafe(row[8]));
            response.setStatus(toStringSafe(row[9]));
            response.setFromAccountNo(toStringSafe(row[10]));
            response.setFromAccountTitle(toStringSafe(row[11]));
            response.setToAccountTitle(toStringSafe(row[12]));
            response.setToAccountNo(toStringSafe(row[13]));
            response.setTransRefNum(toBigDecimal(row[14]));
            response.setCardNo(toStringSafe(row[15]));
            setResponse(res, Constants.ONE, response, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(res, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return res;
    }

    @Override
    public Response agentTracking(AgentTrackingRequest agentTrackingRequest, HttpServletRequest request) {
        Response res = new Response();
        List<Object[]> findTransactions = vwMiniStatementRepo.agentTracking(agentTrackingRequest.getAgentId(), agentTrackingRequest.getAgentName());
        List<SearchTransactionResponse> responseList = new ArrayList<>();
        if (findTransactions.size() > 0) {
            for (Object[] row : findTransactions) {
                SearchTransactionResponse response = new SearchTransactionResponse();
                response.setMwRequestId(toBigDecimal(row[0]));
                response.setTransHeadId(toBigDecimal(row[1]));
                response.setErrorResponse(toStringSafe(row[2]));
                response.setResponseDescr(toStringSafe(row[3]));
                response.setStan(toStringSafe(row[4]));
                response.setRrn(toStringSafe(row[5]));
                response.setAmount(toBigDecimal(row[6]));
                response.setTransDate(toDateSafe(row[7]));
                response.setTransactionType(toStringSafe(row[8]));
                response.setStatus(toStringSafe(row[9]));
                response.setFromAccountNo(toStringSafe(row[10]));
                response.setFromAccountTitle(toStringSafe(row[11]));
                response.setToAccountTitle(toStringSafe(row[12]));
                response.setToAccountNo(toStringSafe(row[13]));
                response.setTransRefNum(toBigDecimal(row[14]));
                response.setCardNo(toStringSafe(row[15]));

                responseList.add(response);
            }
            setResponse(res, Constants.ONE, responseList, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(res, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return res;
    }

    @Override
    public List<TransactionDashboardOne> transactionDashboardOne() {
        List<Object[]> findTransactions = vwMiniStatementRepo.transactionDashboardOne();
        List<TransactionDashboardOne> results = new ArrayList<>();

        for (Object[] result : findTransactions) {
            TransactionDashboardOne data = new TransactionDashboardOne();

            // Cast and map the Object[] to the fields of TransactionDashboardOne
            data.setTotalAmount((BigDecimal) result[0]);  // Assuming the first element is BigDecimal for totalAmount
            data.setTotalCount((BigDecimal) result[1]);   // Assuming the second element is BigDecimal for totalCount
            data.setTransCategory((String) result[2]);    // Assuming the third element is String for transCategory

            results.add(data);
        }
        return results;
    }

    @Override
    public List<TransactionDashboardTwo> getTransactionDashboardTwo() {
        List<Object[]> findTransactions = vwMiniStatementRepo.transactionDashboardTwo();
        List<TransactionDashboardTwo> results = new ArrayList<>();

        for (Object[] result : findTransactions) {
            TransactionDashboardTwo data = new TransactionDashboardTwo();

            // Cast and map the Object[] to the fields of TransactionDashboardOne
            data.setTotalAmount((BigDecimal) result[0]);  // Assuming the first element is BigDecimal for totalAmount
            data.setTotalCount((BigDecimal) result[1]);   // Assuming the second element is BigDecimal for totalCount
            data.setMonth((String) result[2]);    // Assuming the third element is String for transCategory

            results.add(data);
        }
        return results;
    }

    @Override
    public List<CustomerDashboardOne> getCustomerDashboardOne() {
        List<Object[]> findTransactions = vwMiniStatementRepo.getCustomerDashboardOne();
        List<CustomerDashboardOne> results = new ArrayList<>();

        for (Object[] result : findTransactions) {
            CustomerDashboardOne data = new CustomerDashboardOne();

            // Cast and map the Object[] to the fields of TransactionDashboardOne
            data.setTotalAccount((BigDecimal) result[0]);  // Assuming the first element is BigDecimal for totalAmount
            data.setAccountType((String) result[1]);   // Assuming the second element is BigDecimal for totalCount
            // Assuming the third element is String for transCategory

            results.add(data);
        }
        return results;
    }

    @Override
    public List<CustomerDashboardTwo> getCustomerDashboardTwo() {
        List<Object[]> findTransactions = vwMiniStatementRepo.getCustomerDashboardTwo();
        List<CustomerDashboardTwo> results = new ArrayList<>();

        for (Object[] result : findTransactions) {
            CustomerDashboardTwo data = new CustomerDashboardTwo();

            // Cast and map the Object[] to the fields of TransactionDashboardOne
            data.setTotalAccount((BigDecimal) result[0]);  // Assuming the first element is BigDecimal for totalAmount
            data.setMonth((String) result[1]);   // Assuming the second element is BigDecimal for totalCount
            // Assuming the third element is String for transCategory

            results.add(data);
        }
        return results;
    }

    @Override
    public List<AgentDashboardOne> getAgentDashboardOne() {
        List<Object[]> findTransactions = vwMiniStatementRepo.getAgentDashboardOne();
        List<AgentDashboardOne> results = new ArrayList<>();

        for (Object[] result : findTransactions) {
            AgentDashboardOne data = new AgentDashboardOne();

            // Cast and map the Object[] to the fields of TransactionDashboardOne
            data.setTotalAgent((BigDecimal) result[0]);  // Assuming the first element is BigDecimal for totalAmount
            data.setStatus((String) result[1]);   // Assuming the second element is BigDecimal for totalCount
            // Assuming the third element is String for transCategory

            results.add(data);
        }
        return results;
    }

    @Override
    public List<AgentDashboardTwo> getAgentDashboardTwo() {
        List<Object[]> findTransactions = vwMiniStatementRepo.getAgentDashboardTwo();
        List<AgentDashboardTwo> results = new ArrayList<>();

        for (Object[] result : findTransactions) {
            AgentDashboardTwo data = new AgentDashboardTwo();

            // Cast and map the Object[] to the fields of TransactionDashboardOne
            data.setTotalAgent((BigDecimal) result[0]);  // Assuming the first element is BigDecimal for totalAmount
            data.setMonth((String) result[1]);   // Assuming the second element is BigDecimal for totalCount
            // Assuming the third element is String for transCategory

            results.add(data);
        }
        return results;
    }

    @Override
    public List<IndividualDashboardOne> getIndividualDashboardOne() {
        List<Object[]> findTransactions = vwMiniStatementRepo.getIndividualDashboardOne();
        List<IndividualDashboardOne> results = new ArrayList<>();

        for (Object[] result : findTransactions) {
            IndividualDashboardOne data = new IndividualDashboardOne();

            // Cast and map the Object[] to the fields of TransactionDashboardOne
            data.setTotalCustomer((BigDecimal) result[0]);  // Assuming the first element is BigDecimal for totalAmount
            data.setStatus((String) result[1]);   // Assuming the second element is BigDecimal for totalCount
            // Assuming the third element is String for transCategory

            results.add(data);
        }
        return results;
    }

    @Override
    public List<IndividualDashboardTwo> getIndividualDashboardTwo() {
        List<Object[]> findTransactions = vwMiniStatementRepo.getIndividualDashboardTwo();
        List<IndividualDashboardTwo> results = new ArrayList<>();

        for (Object[] result : findTransactions) {
            IndividualDashboardTwo data = new IndividualDashboardTwo();

            // Cast and map the Object[] to the fields of TransactionDashboardOne
            data.setTotalAccounts((BigDecimal) result[0]);  // Assuming the first element is BigDecimal for totalAmount
            data.setMonth((String) result[1]);   // Assuming the second element is BigDecimal for totalCount
            // Assuming the third element is String for transCategory

            results.add(data);
        }
        return results;
    }


    private BigDecimal toBigDecimal(Object obj) {
        return (obj != null) ? new BigDecimal(obj.toString()) : null;
    }

    private Date toDateSafe(Object obj) {
        return (obj instanceof Date) ? (Date) obj : null;
    }

    private String toStringSafe(Object obj) {
        return (obj != null) ? obj.toString() : null;
    }
}
