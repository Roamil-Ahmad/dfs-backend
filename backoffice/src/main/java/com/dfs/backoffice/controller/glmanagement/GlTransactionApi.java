package com.dfs.backoffice.controller.glmanagement;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.GlTransactionRequest;
import com.dfs.backoffice.dto.GlTransactionResponse;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.dto.TitleFetchRequest;
import com.dfs.backoffice.model.TblAccount;
import com.dfs.backoffice.service.GlTransactionService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.GenericResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class GlTransactionApi extends HelperClass {

    Logger LOG = LoggerFactory.getLogger(GlTransactionApi.class);

    @Autowired
    private GlTransactionService glTransactionService;


    @PostMapping(value = "/glTogl", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> glTogl(@RequestBody GlTransactionRequest glTransactionRequest, HttpServletRequest request) {
        Response response = new Response();
        GlTransactionResponse glToGlTransactionResponse = glTransactionService.glToGlTransfer(glTransactionRequest, request);
        if (glToGlTransactionResponse.getResponseStatus() == 1) {
            setResponse(response, Constants.ONE, glToGlTransactionResponse, GenericResponseCode.SUCCESS.getResponseCode(), glToGlTransactionResponse.getResponseDescr());
        } else {
            setResponse(response, Constants.ZERO, glToGlTransactionResponse, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode(), glToGlTransactionResponse.getResponseDescr());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }


    @PostMapping(value = "/walletToGl", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> accountToGl(@RequestBody GlTransactionRequest glTransactionRequest, HttpServletRequest request) {
        Response response = new Response();
        GlTransactionResponse glToGlTransactionResponse = glTransactionService.walletToGlTransfer(glTransactionRequest, request);
        if (glToGlTransactionResponse.getResponseStatus() == 1) {
            setResponse(response, Constants.ONE, glToGlTransactionResponse, GenericResponseCode.SUCCESS.getResponseCode(), glToGlTransactionResponse.getResponseDescr());
        } else {
            setResponse(response, Constants.ZERO, glToGlTransactionResponse, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode(), glToGlTransactionResponse.getResponseDescr());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }


    @PostMapping(value = "/glToWallet", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> glToWallet(@RequestBody GlTransactionRequest glTransactionRequest, HttpServletRequest request) {
        Response response = new Response();
        GlTransactionResponse glToGlTransactionResponse = glTransactionService.glToWalletTransfer(glTransactionRequest, request);
        if (glToGlTransactionResponse.getResponseStatus() == 1) {
            setResponse(response, Constants.ONE, glToGlTransactionResponse, GenericResponseCode.SUCCESS.getResponseCode(), glToGlTransactionResponse.getResponseDescr());
        } else {
            setResponse(response, Constants.ZERO, glToGlTransactionResponse, GenericResponseCode.RECORD_NOT_SAVED.getResponseCode(), glToGlTransactionResponse.getResponseDescr());
        }
        return castResponseToEntity(response, response.getResponseCode());
    }

    @PostMapping(value = "/titleFetch", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> titleFetch(@RequestBody TitleFetchRequest titleFetchRequest, HttpServletRequest request) {
        Response response = new Response();
        TblAccount tblAccount = glTransactionService.getAccountAgainstAccountNo(titleFetchRequest);
        if (tblAccount != null) {
            tblAccount.setAccountTitle(!isNullOrEmpty(tblAccount.getAccountTitle()) ? decrypttWithAes(tblAccount.getAccountTitle()) : "");
            setResponse(response, Constants.ONE, tblAccount, GenericResponseCode.SUCCESS.getResponseCode());
        } else {
            setResponse(response, Constants.ZERO, tblAccount, GenericResponseCode.ACCOUNT_NOT_FOUND.getResponseCode());
        }
        return castResponseToEntity(response, response.getResponseCode());

    }

}
