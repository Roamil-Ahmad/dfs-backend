package com.dfs.backoffice.controller.notification;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.GenerateNotificationRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.service.CommonService;
import com.dfs.backoffice.service.NotificationService;
import com.dfs.backoffice.utils.JwtConstants;
import com.dfs.backoffice.utils.RequestValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationController extends HelperClass {
    @Autowired
    private CommonService commonService;
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/generateNotification")
    ResponseEntity<Response> generateNotification(@RequestBody GenerateNotificationRequest generateNotificationRequest, HttpServletRequest httpServletRequest) throws JsonProcessingException, ExecutionException, InterruptedException {
        RequestValidator.validateGenerateNotificationRequest(generateNotificationRequest);
        CompletableFuture<Response> getResponse = notificationService.notify(generateNotificationRequest, new BigDecimal((String) httpServletRequest.getAttribute(JwtConstants.APP_USER_ID)));
        Response response = getResponse.get();
        return castResponseToEntity(response, response.getResponseCode());
    }
}
