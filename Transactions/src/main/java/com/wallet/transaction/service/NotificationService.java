package com.wallet.transaction.service;

import com.wallet.transaction.model.TblAppUser;

public interface NotificationService {

    void sendNotification(String body, String token);
    void saveNotification(String msg, TblAppUser tblAppUser, String type, String notificationTitle, String notificationMsg);
}
