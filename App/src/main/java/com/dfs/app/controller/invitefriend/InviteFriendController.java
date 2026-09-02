package com.dfs.app.controller.invitefriend;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.InviteFriendRequest;
import com.dfs.app.dto.common.CustomerKycRequest;
import com.dfs.app.dto.common.Request;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.InviteFriendService;
import com.dfs.app.service.ThirdPartyService;
import com.dfs.app.util.Constants;
import com.dfs.app.util.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class InviteFriendController extends HelperClass {
    Logger LOG = LoggerFactory.getLogger(InviteFriendController.class);
    @Autowired
    private InviteFriendService inviteFriendService;
    @Autowired
    private CommonService commonService;


    @PostMapping(value = "/v1/invitefriendreq", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> customerKyc(@RequestBody Request apiRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        InviteFriendRequest inviteFriendRequest = fromJson(convertObjecttoJson(apiRequest.getPayload()), InviteFriendRequest.class);
        HashMap<String, Object> response = inviteFriendService.inviteFriendRequest(inviteFriendRequest, apiRequest, httpServletRequest.getHeader(Constants.AUTHORIZATION));
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

    @GetMapping("/v1/getinvitestats/{id}")
    public ResponseEntity<HashMap<String, Object>> getAllProvince(@PathVariable String id, HttpServletRequest httpServletRequest) {
        HashMap<String, Object> response = inviteFriendService.getInviteStats(Long.parseLong(id));
        return getCustomizedResponseFormat(HttpStatus.OK, response);
    }

}

