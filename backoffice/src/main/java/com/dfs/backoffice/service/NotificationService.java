package com.dfs.backoffice.service;


import com.dfs.backoffice.dto.GenerateNotificationRequest;
import com.dfs.backoffice.dto.Response;
import org.springframework.web.servlet.function.EntityResponse;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

public interface NotificationService {

    CompletableFuture<Response> notify(GenerateNotificationRequest generateNotificationRequest, BigDecimal userId);
}
