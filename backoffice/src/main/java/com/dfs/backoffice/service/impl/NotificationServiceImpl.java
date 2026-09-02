package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.controller.HelperClass;
import com.dfs.backoffice.dto.GenerateNotificationRequest;
import com.dfs.backoffice.dto.Response;
import com.dfs.backoffice.model.TblSmsMessage;
import com.dfs.backoffice.repo.TblSmsMessageRepo;
import com.dfs.backoffice.service.NotificationService;
import com.dfs.backoffice.utils.Constants;
import com.dfs.backoffice.utils.CustomException;
import com.dfs.backoffice.utils.GenericResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificationServiceImpl extends HelperClass implements NotificationService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TblSmsMessageRepo tblSmsMessageRepo;

    @Async
    public CompletableFuture<Response> notify(GenerateNotificationRequest generateNotificationRequest, BigDecimal userId) {
        Response response = new Response();

        if (generateNotificationRequest.getType().equalsIgnoreCase("E")) {
            return sendSimpleEmail(generateNotificationRequest.getEmail(),
                    generateNotificationRequest.getSubject(),
                    generateNotificationRequest.getSms())
                    .thenApply(emailSent -> {
                        if (emailSent) {
                            setResponse(response, Constants.ONE, null, GenericResponseCode.SUCCESS.getResponseCode());
                        } else {
                            throw new CustomException(GenericResponseCode.TECHNICAL_ISSUE.getResponseCode());
                        }
                        return response;
                    });
        } else if (generateNotificationRequest.getType().equalsIgnoreCase("M")) {
            TblSmsMessage tblSmsMessage = new TblSmsMessage();
            tblSmsMessage.setMobileNo(generateNotificationRequest.getMobileNumber());
            tblSmsMessage.setSendFlag("0");
            tblSmsMessage.setCreatedate(new Date());
            tblSmsMessage.setCreateuser(userId);
            tblSmsMessage.setSmsMessageTemplateId(generateNotificationRequest.getTemplateId());
            tblSmsMessage.setMessage(generateNotificationRequest.getSms());
            tblSmsMessageRepo.saveAndFlush(tblSmsMessage);
            setResponse(response, Constants.ONE, null, GenericResponseCode.SUCCESS.getResponseCode());
            return CompletableFuture.completedFuture(response);
        }

        return CompletableFuture.completedFuture(response);
    }


    @Async
    public CompletableFuture<Boolean> sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }
}
