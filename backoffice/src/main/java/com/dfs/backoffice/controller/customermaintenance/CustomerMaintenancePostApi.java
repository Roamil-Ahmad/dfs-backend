package com.dfs.backoffice.controller.customermaintenance;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.*;
import com.dfs.backoffice.model.TblCustomer;
import com.dfs.backoffice.model.VwMiniStatement;
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
public class CustomerMaintenancePostApi extends HelperClass {
    @Autowired
    private CustomerMaintenanceService customerMaintenanceService;

    @PostMapping(value = "/searchCustomer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> searchCustomer(@RequestBody SearchCustomerRequest searchCustomerRequest, HttpServletRequest request) {
        Response response = new Response();
        RequestValidator.searchCustomerJsonValidate(searchCustomerRequest);
        List<SearchCustomerResponse> tblCustomer = customerMaintenanceService.searchCustomer(searchCustomerRequest);
        if (!isNullOrEmpty(tblCustomer)) {
            setResponse(response, Constants.ONE, tblCustomer, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblCustomer, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/getLastTenTransactions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> miniStatement(@RequestBody MiniStatementRequest miniStatementRequest, HttpServletRequest request) {
        Response response = new Response();
        List<VwMiniStatement> vwMiniStatements = customerMaintenanceService.getLastTenTransactions(miniStatementRequest, request);
        if (!isNullOrEmpty(vwMiniStatements)) {
            setResponse(response, Constants.ONE, vwMiniStatements, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, vwMiniStatements, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/updateCustomer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateCustomer(@RequestBody UpdateCustomerRequest updateCustomerRequest, HttpServletRequest request) {
        Response response = new Response();
        response = customerMaintenanceService.updateCustomer(updateCustomerRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @GetMapping("/getCustomer/{nidNo}")
    public ResponseEntity<Response> getCustomerByNidNo(@PathVariable String nidNo, HttpServletRequest request) {
        Response response = new Response();
        TblCustomer tblCustomer = customerMaintenanceService.getCustomerByNidNo(nidNo);
        if (tblCustomer != null) {
            tblCustomer.setAddressP(tblCustomer.getAddressP() != null ? decrypttWithAes(tblCustomer.getAddressP()) : "");
            tblCustomer.setFatherName(tblCustomer.getFatherName() != null ? decrypttWithAes(tblCustomer.getFatherName()) : "");
            tblCustomer.setFullName(tblCustomer.getFullName() != null ? decrypttWithAes(tblCustomer.getFullName()) : "");
            tblCustomer.setGrandfatherName(tblCustomer.getGrandfatherName() != null ? decrypttWithAes(tblCustomer.getGrandfatherName()) : "");
            tblCustomer.setPob(tblCustomer.getPob() != null ? decrypttWithAes(tblCustomer.getPob()) : "");
            tblCustomer.setNidNo(tblCustomer.getNidNo() != null ? decrypttWithAes(tblCustomer.getNidNo()) : "");
            tblCustomer.setEmail(tblCustomer.getEmail() != null ? decrypttWithAes(tblCustomer.getEmail()) : "");
            setResponse(response, Constants.ONE, tblCustomer, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblCustomer, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/updateAccountStatus", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> updateStatus(@RequestBody UpdateStatusRequest updateStatusRequest, HttpServletRequest request) {
        Response response = new Response();
        response = customerMaintenanceService.updateAccountStatus(updateStatusRequest, request);
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/getCardLastTransactions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getCardLastTransactions(@RequestBody MiniStatementRequest miniStatementRequest, HttpServletRequest request) {
        Response response = new Response();
        List<VwMiniStatement> vwMiniStatements = customerMaintenanceService.getCardLastTransactions(miniStatementRequest, request);
        if (!isNullOrEmpty(vwMiniStatements)) {
            setResponse(response, Constants.ONE, vwMiniStatements, GenericResponseCode.RECORD_FOUND.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, vwMiniStatements, GenericResponseCode.RECORD_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }
}
