package com.dfs.backoffice.controller.agentonboarding;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.AgentApprovalRequest;
import com.dfs.backoffice.dto.CreateAgentAccountRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.dto.SearchAgent;
import com.dfs.backoffice.model.TblAgent;
import com.dfs.backoffice.service.AgentOnBoardingService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.GenericResponseCode;
import com.dfs.backoffice.utils.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class AgentOnBoardingPostApi extends HelperClass {

    @Autowired
    private AgentOnBoardingService agentOnBoardingService;

    @PostMapping(value = "/createAgentAccount", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createAgentAccount(@RequestBody CreateAgentAccountRequest createAgentAccountRequest, HttpServletRequest request) {
        RequestValidator.createAgentAccountJsonValidate(createAgentAccountRequest);
        Response response = agentOnBoardingService.createAgentAccount(createAgentAccountRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/updateAgentAccount", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateAgentAccount(@RequestBody CreateAgentAccountRequest createAgentAccountRequest, HttpServletRequest request) {
        RequestValidator.updateAgentAccountJsonValidate(createAgentAccountRequest);
        Response response = agentOnBoardingService.updateAgentAccount(createAgentAccountRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/getAllAgents", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getAllAgents(@RequestBody SearchAgent searchAgent, HttpServletRequest request) {
        Response response = new Response();
        RequestValidator.searchAgentJsonValidate(searchAgent);
        List<TblAgent> tblAgents = agentOnBoardingService.getAllAgents(searchAgent);
        if (!tblAgents.isEmpty()) {
            for (TblAgent tblAgent : tblAgents) {
                tblAgent.setEmail(!isNullOrEmpty(tblAgent.getEmail()) ? decrypttWithAes(tblAgent.getEmail()) : "");
                tblAgent.setPermanentAddress(!isNullOrEmpty(tblAgent.getPermanentAddress()) ? decrypttWithAes(tblAgent.getPermanentAddress()) : "");
                tblAgent.setFatherHusbandName(!isNullOrEmpty(tblAgent.getFatherHusbandName()) ? decrypttWithAes(tblAgent.getFatherHusbandName()) : "");
                tblAgent.setMobile(!isNullOrEmpty(tblAgent.getMobile()) ? decrypttWithAes(tblAgent.getMobile()) : "");
                tblAgent.setName(!isNullOrEmpty(tblAgent.getName()) ? decrypttWithAes(tblAgent.getName()) : "");
                tblAgent.setNidNo(!isNullOrEmpty(tblAgent.getNidNo()) ? decrypttWithAes(tblAgent.getNidNo()) : "");
                tblAgent.setResidentialAddress(!isNullOrEmpty(tblAgent.getResidentialAddress()) ? decrypttWithAes(tblAgent.getResidentialAddress()) : "");
            }
            setResponse(response, Constants.ONE, tblAgents, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblAgents, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/agentApproval", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> agentApproval(@RequestBody AgentApprovalRequest agentApprovalRequest, HttpServletRequest request) throws JsonProcessingException {
        Response response = agentOnBoardingService.agentApproval(agentApprovalRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }
}
