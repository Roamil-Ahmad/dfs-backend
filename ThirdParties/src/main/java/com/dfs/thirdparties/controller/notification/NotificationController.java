package com.dfs.thirdparties.controller.notification;

import com.dfs.thirdparties.commons.HelperClass;
import com.dfs.thirdparties.dto.GenerateNotificationRequest;
import com.dfs.thirdparties.dto.VerifyOtpRequest;
import com.dfs.thirdparties.dto.common.Request;
import com.dfs.thirdparties.service.CommonService;
import com.dfs.thirdparties.service.NotificationService;
import com.dfs.thirdparties.util.RequestValidator;
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
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationController extends HelperClass {
    @Autowired
    private CommonService commonService;
    @Autowired
    private NotificationService notificationService;
    @PostMapping("/generateNotification")
    ResponseEntity<HashMap<String, Object>> generateNotification(@RequestBody Request request, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        BigDecimal userId=commonService.authenticateHeaderAndDeviceLoggedIn(httpServletRequest,request);
        GenerateNotificationRequest generateNotificationRequest=fromJson(convertObjecttoJson(request.getPayload()),GenerateNotificationRequest.class);
        RequestValidator.validateGenerateNotificationRequest(generateNotificationRequest);
        HashMap<String, Object> generateOtpResponse=notificationService.notify(generateNotificationRequest,userId);
        return getCustomizedResponseFormat(HttpStatus.OK,generateOtpResponse);

    }
}
