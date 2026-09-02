package com.dfs.thirdparties.service;

import com.dfs.thirdparties.dto.GenerateNotificationRequest;

import java.math.BigDecimal;
import java.util.HashMap;

public interface NotificationService {

    HashMap<String, Object> notify(GenerateNotificationRequest generateNotificationRequest, BigDecimal userId);
    boolean sendSimpleEmail(String to, String subject, String text);
}
