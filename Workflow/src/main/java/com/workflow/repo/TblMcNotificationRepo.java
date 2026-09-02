package com.workflow.repo;

import com.workflow.modal.TblMcNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface TblMcNotificationRepo extends JpaRepository<TblMcNotification, Long> {
    @Query(value = "SELECT MC_PENDING_REQUEST_ID FROM TBL_MC_NOTIFICATION WHERE IS_ACTIVE='Y'  AND USER_ID=:userId", nativeQuery = true)
    List<BigDecimal> findNotification(long userId);

    List<TblMcNotification> findByMcPendingRequestIdAndIsActive(BigDecimal mcRequestId, String yes);
}
