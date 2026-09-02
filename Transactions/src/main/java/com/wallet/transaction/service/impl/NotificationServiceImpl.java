package com.wallet.transaction.service.impl;

import com.wallet.transaction.controller.HelperClass;
import com.wallet.transaction.model.TblAppUser;
import com.wallet.transaction.model.TblNotification;
import com.wallet.transaction.repo.TblNotificationRepo;
import com.wallet.transaction.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class NotificationServiceImpl extends HelperClass implements NotificationService {
    @Autowired
    private TblNotificationRepo tblNotificationRepo;
    @Value("${push.notification.url}")
    private String notificationUrl;

    @Override
    @Async
    public void sendNotification(String body, String token) {
        getResponseFromGetAPI(notificationUrl + body + "/" + token);
    }

    @Override
    @Async
    public void saveNotification(String msg, TblAppUser tblAppUser, String type, String notificationTitle, String notificationMsg) {
        TblNotification tblNotification = new TblNotification();
        tblNotification.setCreatedate(new Date());
        tblNotification.setCreateuser(new BigDecimal(tblAppUser.getAppUserId()));
        tblNotification.setNotificationMessage(notificationMsg);
        tblNotification.setNotificationTitle(notificationTitle);
        tblNotification.setAppUserId(new BigDecimal(tblAppUser.getAppUserId()));
        tblNotification.setNotificationType(type);
        tblNotification.setIsActive("Y");
        tblNotificationRepo.save(tblNotification);
    }
}
