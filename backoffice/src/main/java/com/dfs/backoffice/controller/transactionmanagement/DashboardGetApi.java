package com.dfs.backoffice.controller.transactionmanagement;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.TblAppUser;
import com.dfs.backoffice.model.TblUser;
import com.dfs.backoffice.service.TransactionService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.GenericResponseCode;
import com.dfs.backoffice.utils.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class DashboardGetApi extends HelperClass {
    @Autowired
    private TransactionService searchTransaction;

    @GetMapping("/getTransactionDashboardOne")
    public ResponseEntity<Response> transactionDashboardOne(HttpServletRequest request) {
        Response response = new Response();
        List<TransactionDashboardOne> transactionDashboardOneLits = searchTransaction.transactionDashboardOne();
        if (transactionDashboardOneLits != null) {
            setResponse(response, Constants.ONE, transactionDashboardOneLits, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, transactionDashboardOneLits, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/getTransactionDashboardTwo")
    public ResponseEntity<Response> transactionDashboardTwo(HttpServletRequest request) {
        Response response = new Response();
        List<TransactionDashboardTwo> transactionDashboardOneLits = searchTransaction.getTransactionDashboardTwo();
        if (transactionDashboardOneLits != null) {
            setResponse(response, Constants.ONE, transactionDashboardOneLits, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, transactionDashboardOneLits, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/getCustomerDashboardOne")
    public ResponseEntity<Response> getCustomerDashboardOne(HttpServletRequest request) {
        Response response = new Response();
        List<CustomerDashboardOne> customerDashboardOne = searchTransaction.getCustomerDashboardOne();
        if (customerDashboardOne != null) {
            setResponse(response, Constants.ONE, customerDashboardOne, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, customerDashboardOne, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }


    @GetMapping("/getCustomerDashboardTwo")
    public ResponseEntity<Response> getCustomerDashboardTwo(HttpServletRequest request) {
        Response response = new Response();
        List<CustomerDashboardTwo> customerDashboardOne = searchTransaction.getCustomerDashboardTwo();
        if (customerDashboardOne != null) {
            setResponse(response, Constants.ONE, customerDashboardOne, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, customerDashboardOne, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/getAgentDashboardOne")
    public ResponseEntity<Response> getAgentDashboardOne(HttpServletRequest request) {
        Response response = new Response();
        List<AgentDashboardOne> agentDashboardOne = searchTransaction.getAgentDashboardOne();
        if (agentDashboardOne != null) {
            setResponse(response, Constants.ONE, agentDashboardOne, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, agentDashboardOne, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/getAgentDashboardTwo")
    public ResponseEntity<Response> getAgentDashboardTwo(HttpServletRequest request) {
        Response response = new Response();
        List<AgentDashboardTwo> agentDashboardOne = searchTransaction.getAgentDashboardTwo();
        if (agentDashboardOne != null) {
            setResponse(response, Constants.ONE, agentDashboardOne, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, agentDashboardOne, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/getIndividualDashboardOne")
    public ResponseEntity<Response> getIndividualDashboardOne(HttpServletRequest request) {
        Response response = new Response();
        List<IndividualDashboardOne> individualDashboardOne = searchTransaction.getIndividualDashboardOne();
        if (individualDashboardOne != null) {
            setResponse(response, Constants.ONE, individualDashboardOne, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, individualDashboardOne, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/getIndividualDashboardTwo")
    public ResponseEntity<Response> getIndividualDashboardTwo(HttpServletRequest request) {
        Response response = new Response();
        List<IndividualDashboardTwo> individualDashboardTwo = searchTransaction.getIndividualDashboardTwo();
        if (individualDashboardTwo != null) {
            setResponse(response, Constants.ONE, individualDashboardTwo, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, individualDashboardTwo, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }
}
