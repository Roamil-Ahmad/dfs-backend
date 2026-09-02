package com.workflow.repo;

import com.workflow.modal.TblAlertDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface TblAlertDetailRepo extends JpaRepository<TblAlertDetail,Long> {
    @Query(value = "SELECT * FROM TBL_ALERT_DETAIL WHERE STATUS = 'N'", nativeQuery = true)
    List<TblAlertDetail> getAlerts();

    @Modifying
    @Transactional
    @Query(value = "UPDATE TblAlertDetail a SET a.status = 'S' WHERE a.alertDetailId = :alertDetailId")
    Integer updateTblMinorAlert(long alertDetailId);
}
