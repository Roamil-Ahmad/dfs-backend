package com.dfs.app.controller.favpay;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.AddFavPayRequest;
import com.dfs.app.dto.GetBalanceRequest;
import com.dfs.app.dto.UpdateFavPayRequest;
import com.dfs.app.dto.common.Request;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.FavPayService;
import com.dfs.app.service.LoginService;
import com.dfs.app.util.Constants;
import com.dfs.app.util.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FavPayController extends HelperClass {
    @Autowired
    private FavPayService favPayService;
    @Autowired
    private CommonService commonService;


    @PostMapping("/v1/addfavpay")
    public ResponseEntity<HashMap<String, Object>> addFavPay(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException, ParseException {

        BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        AddFavPayRequest favPayRequest = fromJson(convertObjecttoJson(request.getPayload()), AddFavPayRequest.class);
        RequestValidator.validateAddFavPayRequest(favPayRequest, request);
        HashMap<String, Object> response = favPayService.saveBeneficary(favPayRequest,userId, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }
    @PostMapping("/v1/getallfavpay")
    public ResponseEntity<HashMap<String, Object>> getAllFavpay(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        GetBalanceRequest getBalanceRequest = fromJson(convertObjecttoJson(request.getPayload()), GetBalanceRequest.class);
        RequestValidator.validateGetBalanceRequest(getBalanceRequest, request);
        HashMap<String, Object> response = favPayService.getAllFavpay(getBalanceRequest, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }
    @PostMapping("/v1/updatefavpay")
    public ResponseEntity<HashMap<String, Object>> updateFavPay(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        UpdateFavPayRequest updateFavPayRequest = fromJson(convertObjecttoJson(request.getPayload()), UpdateFavPayRequest.class);
        RequestValidator.validateUpdateFavPayRequest(updateFavPayRequest, request);
        HashMap<String, Object> response = favPayService.updateFavPay(updateFavPayRequest,userId, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }

}
