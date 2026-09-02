package com.dfs.backoffice.controller.glmanagement;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.GlCreationRequest;
import com.dfs.backoffice.dto.GlTreeChildResponse;
import com.dfs.backoffice.dto.GlTreeResponse;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.model.TblGlAccount;
import com.dfs.backoffice.service.GlCreationService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.GenericResponseCode;
import com.dfs.backoffice.utils.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class GlCreationPostApis extends HelperClass {

    Logger LOG = LoggerFactory.getLogger(GlCreationPostApis.class);

    @Autowired
    private GlCreationService glCreationService;


    @PostMapping(value = "/createGlAccount", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> createGlAccount(@RequestBody GlCreationRequest glCreationRequest, HttpServletRequest request) {
        RequestValidator.createGlAccountJsonValidate(glCreationRequest);
        Response response = glCreationService.createGlAccount(glCreationRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/updateGlAccount", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateGlAccount(@RequestBody GlCreationRequest glCreationRequest, HttpServletRequest request) {
        RequestValidator.updateGlAccountJsonValidate(glCreationRequest);
        Response response = glCreationService.updateGlAccount(glCreationRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping(value = "/getAllGlAccount")
    public ResponseEntity<Response> getAllGlAccount(HttpServletRequest request) {
        Response response = new Response();
        List<GlTreeResponse> glTreeResponses = new ArrayList<GlTreeResponse>();
        List<TblGlAccount> parentGlAccounts = glCreationService.getAllParentGeneralLedgers();
        if (!parentGlAccounts.isEmpty()) {
            for (TblGlAccount tblGlAccount : parentGlAccounts) {
                GlTreeResponse glTreeResponse = new GlTreeResponse();

                glTreeResponse.setKey(tblGlAccount.getGlAccountCode());
                glTreeResponse.setData(tblGlAccount);

                List<GlTreeChildResponse> glTreeChildResponses = new ArrayList<GlTreeChildResponse>();

                List<TblGlAccount> childGlAccounts = glCreationService
                        .getChildGlsAgainstParent(tblGlAccount.getGlAccountId());
                if (childGlAccounts != null) {
                    for (TblGlAccount childGlAccount : childGlAccounts) {
                        GlTreeChildResponse glTreeChildResponse = new GlTreeChildResponse();
                        glTreeChildResponse
                                .setKey(tblGlAccount.getGlAccountCode() + "-" + childGlAccount.getGlAccountCode());
                        glTreeChildResponse.setData(childGlAccount);
                        glTreeChildResponses.add(glTreeChildResponse);
                    }
                }
                glTreeResponse.setChildren(glTreeChildResponses);
                glTreeResponses.add(glTreeResponse);
            }
            if (!glTreeResponses.isEmpty()) {
                setResponse(response, Constants.ONE, glTreeResponses, GenericResponseCode.RECORD_FOUND.getResponseCode());
            } else {
                setResponse(response, Constants.ZERO, parentGlAccounts, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
            }
        } else {
            setResponse(response, Constants.ZERO, parentGlAccounts, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/getGlAccountById/{glAccountId}")
    public ResponseEntity<Response> getGlAccountById(@PathVariable String glAccountId, HttpServletRequest request) {
        Response response = new Response();
        TblGlAccount tblGlAccount = glCreationService.getGlAccountById(Long.parseLong(glAccountId));
        if (tblGlAccount != null) {
            setResponse(response, Constants.ONE, tblGlAccount, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblGlAccount, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }
}
