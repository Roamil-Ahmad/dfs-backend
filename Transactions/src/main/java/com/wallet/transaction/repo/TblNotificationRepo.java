package com.wallet.transaction.repo;

import com.wallet.transaction.model.TblNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface TblNotificationRepo extends JpaRepository<TblNotification,Long> {

    List<TblNotification> findByNotificationTypeAndAppUserId(String notificationType, BigDecimal appUserId);
}
