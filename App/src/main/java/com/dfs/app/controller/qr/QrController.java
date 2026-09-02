package com.dfs.app.controller.qr;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.DecodeQrRequest;
import com.dfs.app.dto.QrRequest;
import com.dfs.app.dto.common.Request;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.LoginService;
import com.dfs.app.service.QrService;
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
public class QrController extends HelperClass {

    @Autowired
    private CommonService commonService;
    @Autowired
    private QrService qrService;

    @PostMapping("/v1/decodeqr")
    public ResponseEntity<HashMap<String, Object>> decodeQr(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException, ParseException {
        BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        DecodeQrRequest decodeQrRequest = fromJson(convertObjecttoJson(request.getPayload()), DecodeQrRequest.class);
        RequestValidator.validateDecodeQrRequest(decodeQrRequest, request);
        HashMap<String, Object> response = qrService.decodeQr(decodeQrRequest,userId, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }
    @PostMapping("/v1/generateQr")
    public ResponseEntity<HashMap<String, Object>> generateQr(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException, ParseException {
        BigDecimal userId=commonService.authenticateHeaderAndDevice(httpServletRequest, request, Constants.AFTER_LOGIN);
        QrRequest qrRequest = fromJson(convertObjecttoJson(request.getPayload()), QrRequest.class);
        RequestValidator.validateQrRequest(qrRequest, request);
        HashMap<String, Object> response = qrService.generateQr(qrRequest,userId, request);
        return getCustomizedResponseFormat(HttpStatus.OK, response);

    }

}
