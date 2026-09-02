package com.dfs.backoffice.controller.agentmaintenance;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.service.AgentMaintenanceService;
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
public class AgentMaintenanceController extends HelperClass {
    @Autowired
    private AgentMaintenanceService agentMaintenanceService;

    @PostMapping(value = "/searchAgents", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> searchAgent(@RequestBody SearchCustomerRequest searchCustomerRequest, HttpServletRequest request) {
        Response response = new Response();
        RequestValidator.searchCustomerJsonValidate(searchCustomerRequest);
        List<SearchAgentResponse> tblCustomer = agentMaintenanceService.searchAgents(searchCustomerRequest);
        if (!isNullOrEmpty(tblCustomer)) {
            setResponse(response, Constants.ONE, tblCustomer, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblCustomer, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/updateAgent", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateAgent(@RequestBody UpdateCustomerRequest updateCustomerRequest, HttpServletRequest request) {
        Response response = agentMaintenanceService.updateAgent(updateCustomerRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/getAgentDetail", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getAgentDetail(@RequestBody AgentDetailRequest agentDetailRequest, HttpServletRequest request) {
        Response response = new Response();
        RequestValidator.agentDetailJsonValidate(agentDetailRequest);
        AgentDetailResponse agentDetail = agentMaintenanceService.getAgentDetail(agentDetailRequest);
        if (agentDetail != null) {
            setResponse(response, Constants.ONE, agentDetail, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, null, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }
}
