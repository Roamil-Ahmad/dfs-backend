package com.dfs.backoffice.controller.agentonboarding;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.model.TblAgent;
import com.dfs.backoffice.service.AgentOnBoardingService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class AgentOnBoardingGetApi extends HelperClass {

    @Autowired
    private AgentOnBoardingService agentOnBoardingService;

    @GetMapping(value = "/getAgentById/{agentId}")
    public ResponseEntity<Response> getAgentById(@PathVariable String agentId, HttpServletRequest request) {
        Response response = new Response();
        TblAgent tblAgent = agentOnBoardingService.getAgentById(agentId);
        tblAgent.setEmail(!isNullOrEmpty(tblAgent.getEmail()) ? decrypttWithAes(tblAgent.getEmail()) : "");
        tblAgent.setFatherHusbandName(!isNullOrEmpty(tblAgent.getFatherHusbandName()) ? decrypttWithAes(tblAgent.getFatherHusbandName()) : "");
        tblAgent.setMobile(!isNullOrEmpty(tblAgent.getMobile()) ? decrypttWithAes(tblAgent.getMobile()) : "");
        tblAgent.setName(!isNullOrEmpty(tblAgent.getName()) ? decrypttWithAes(tblAgent.getName()) : "");
        tblAgent.setNidNo(!isNullOrEmpty(tblAgent.getNidNo()) ? decrypttWithAes(tblAgent.getNidNo()) : "");
        tblAgent.setResidentialAddress(!isNullOrEmpty(tblAgent.getResidentialAddress()) ? decrypttWithAes(tblAgent.getResidentialAddress()) : "");
        tblAgent.setGrandfatherName(!isNullOrEmpty(tblAgent.getGrandfatherName()) ? decrypttWithAes(tblAgent.getGrandfatherName()) : "");
        tblAgent.setLastName(!isNullOrEmpty(tblAgent.getLastName()) ? decrypttWithAes(tblAgent.getLastName()) : "");
        tblAgent.setPermanentAddress(!isNullOrEmpty(tblAgent.getPermanentAddress()) ? decrypttWithAes(tblAgent.getPermanentAddress()) : "");
        tblAgent.setPob(!isNullOrEmpty(tblAgent.getPob()) ? decrypttWithAes(tblAgent.getPob()) : "");
        setResponse(response, Constants.ONE, tblAgent, GenericResponseCode.RECORD_FOUND.getResponseCode());
        return castResponseToEntity(response, response.getResponseCode());
    }

}
