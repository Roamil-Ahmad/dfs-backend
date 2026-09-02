package com.dfs.backoffice.controller.configuration;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.dto.SearchTransDocs;
import com.dfs.backoffice.model.LkpAccountType;
import com.dfs.backoffice.model.TblTransDoc;
import com.dfs.backoffice.service.ConfigurationService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ConfigurationGetApi extends HelperClass {
    @Autowired
    private ConfigurationService configurationService;

    @GetMapping("/getAllLkpAccountType")
    public ResponseEntity<Response> getAllLkpAccountType(HttpServletRequest request) {
        Response response = new Response();
        List<LkpAccountType> lkpAccountTypes = configurationService.getAllLkpAccountType();
        if (!lkpAccountTypes.isEmpty()) {
            setResponse(response, Constants.ONE, lkpAccountTypes, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, lkpAccountTypes, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/getAllLkpAccountById/{id}")
    public ResponseEntity<Response> getAllLkpAccountTypeByid(@PathVariable String id, HttpServletRequest request) {
        Response response = new Response();
        LkpAccountType lkpAccountTypes = configurationService.getAllLkpAccountTypeByid(Long.parseLong(id));
        setResponse(response, Constants.ONE, lkpAccountTypes, GenericResponseCode.RECORD_FOUND.getResponseCode());
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/getTransDocsById/{id}")
    public ResponseEntity<Response> getTransDocsById(@PathVariable String id, HttpServletRequest request) {
        Response response = new Response();
        TblTransDoc transDoc = configurationService.getTransDocsById(id);
        setResponse(response, Constants.ONE, transDoc, GenericResponseCode.RECORD_FOUND.getResponseCode());
        return castResponseToEntity(response, response.getResponseCode());
    }
}
