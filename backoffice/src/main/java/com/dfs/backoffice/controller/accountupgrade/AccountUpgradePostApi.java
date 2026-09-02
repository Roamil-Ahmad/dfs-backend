package com.dfs.backoffice.controller.accountupgrade;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.SearchKycRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.dto.UpdateKycRequest;
import com.dfs.backoffice.service.AccountUpgradeService;
import com.dfs.backoffice.utils.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class AccountUpgradePostApi extends HelperClass {
    @Autowired
    private AccountUpgradeService accountUpgradeService;

    @PostMapping(value = "/searchKyc", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> searchKyc(@RequestBody SearchKycRequest searchKycRequest, HttpServletRequest request) {
        RequestValidator.searchKycJsonValidate(searchKycRequest);
        Response response = accountUpgradeService.searchKyc(searchKycRequest);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping(value = "/searchKycById/{id}")
    public ResponseEntity<Response> searchKycById(@PathVariable String id, HttpServletRequest request) {
        Response response = accountUpgradeService.searchKycById(id);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/updateKyc", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateKyc(@RequestBody UpdateKycRequest updateKycRequest, HttpServletRequest request) {
        RequestValidator.updateKycJsonValidate(updateKycRequest);
        Response response = accountUpgradeService.updateKyc(updateKycRequest,request);
        return castResponseToEntity(response, response.getResponseCode());
    }
}
