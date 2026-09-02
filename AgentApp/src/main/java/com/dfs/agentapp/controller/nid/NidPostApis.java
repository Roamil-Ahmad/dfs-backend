package com.dfs.agentapp.controller.nid;

import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.NidBvsRequest;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.NidService;
import com.dfs.agentapp.util.Constants;
import com.dfs.agentapp.util.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NidPostApis extends HelperClass {
    Logger LOG = LoggerFactory.getLogger(NidPostApis.class);
    @Autowired
    private CommonService commonService;
    @Autowired
    private NidService nidService;
    @PostMapping("/v1/bioverisys")
    public ResponseEntity<HashMap<String, Object>> bioverisys(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest,request, Constants.AFTER_LOGIN);
        NidBvsRequest nidBvsRequest = fromJson(convertObjecttoJson(request.getPayload()), NidBvsRequest.class);
        RequestValidator.validateNidBvsRequest(nidBvsRequest,request);
        HashMap<String, Object> response=nidService.bioverisys(nidBvsRequest,request,userId);
        return getCustomizedResponseFormat(HttpStatus.OK,response);

    }
}
