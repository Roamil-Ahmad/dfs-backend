package com.dfs.backoffice.controller.configuration;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.TblCustomer;
import com.dfs.backoffice.model.TblTransDoc;
import com.dfs.backoffice.model.VwMiniStatement;
import com.dfs.backoffice.service.ConfigurationService;
import com.dfs.backoffice.service.CustomerMaintenanceService;
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
public class ConfigurationPostApi extends HelperClass {
    @Autowired
    private ConfigurationService configurationService;

    @PostMapping(value = "/saveAccountType", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> saveAccountType(@RequestBody AccountTypeCreation accountTypeCreation, HttpServletRequest request) {
        RequestValidator.accountTypeCreationJsonValidate(accountTypeCreation);
        Response response = configurationService.saveAccountType(accountTypeCreation, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/updateAccountType", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateAccountType(@RequestBody AccountTypeUpdate accountTypeUpdate, HttpServletRequest request) {
        RequestValidator.accountTypeUpdateJsonValidate(accountTypeUpdate);
        Response response = configurationService.updateAccountType(accountTypeUpdate, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/saveTransDocs", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> saveTransDocs(@RequestBody TransDocsRequest transDocsRequest, HttpServletRequest request) {
        RequestValidator.saveTransDocsJsonValidate(transDocsRequest);
        Response response = configurationService.saveTransDocs(transDocsRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/updateTransDocs", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateTransDocs(@RequestBody TransDocsRequest transDocsRequest, HttpServletRequest request) {
        RequestValidator.updateTransDocsJsonValidate(transDocsRequest);
        Response response = configurationService.updateTransDocs(transDocsRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/getAllTransDocs", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getAllTransDocs(@RequestBody SearchTransDocs searchTransDocs, HttpServletRequest request) {
        Response response = new Response();
        RequestValidator.getAllTransDocsJsonValidate(searchTransDocs);
        List<TblTransDoc> tblTransDocs = configurationService.getAllTransDocs(searchTransDocs);
        if (!tblTransDocs.isEmpty()) {
            setResponse(response, Constants.ONE, tblTransDocs, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblTransDocs, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }
}
