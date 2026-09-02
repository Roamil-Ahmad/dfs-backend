package com.notification.firbase.controller;


import com.notification.firbase.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping(value = "/api/notify/{body}/{token}")
    public ResponseEntity<HashMap<String, Object>> sendNotification(@PathVariable String body, @PathVariable String token) {
        try {
            String result = notificationService.sendNotification("DFS", body, token);
            return getCustomizedResponseFormat(HttpStatus.OK, "000", "SUCCESS", result, "/api/notify");
        } catch (Exception e) {
            return getCustomizedResponseFormat(HttpStatus.BAD_REQUEST, "001", "FAILED", e.getMessage(), "/api/notify");
        }
    }

    public ResponseEntity<HashMap<String, Object>> getCustomizedResponseFormat(HttpStatus httpStatus, String responseCode, String responseMessage, Object payload, String endpoint) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("responsecode", responseCode);
        map.put("messages", responseMessage);
        map.put("data", payload);
        System.out.println("This is result ===" + map);
        return ResponseEntity.status(httpStatus).body(map);
    }
}
