package com.dfs.backoffice.controller.transactionmanagement;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.TblTransDoc;
import com.dfs.backoffice.service.ConfigurationService;
import com.dfs.backoffice.service.TransactionService;
import com.dfs.backoffice.utils.Constants;
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
public class TransactionManagementPostApi extends HelperClass {
    @Autowired
    private TransactionService searchTransaction;

    @PostMapping(value = "/searchTransaction", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> searchTransaction(@RequestBody SearchTransactionRequest searchTransactionRequest, HttpServletRequest request) {
        RequestValidator.searchTransactionJsonValidate(searchTransactionRequest);
        Response response = searchTransaction.searchTransaction(searchTransactionRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/transactionDetail", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> transactionDetail(@RequestBody TransactionDetailRequest transactionDetailRequest, HttpServletRequest request) {
        RequestValidator.transactionDetailJsonValidate(transactionDetailRequest);
        Response response = searchTransaction.transactionDetail(transactionDetailRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }


    @PostMapping(value = "/agentTracking", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> agentTracking(@RequestBody AgentTrackingRequest agentTrackingRequest, HttpServletRequest request) {
        Response response = searchTransaction.agentTracking(agentTrackingRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

}
