package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.TblReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblReportRepo extends JpaRepository<TblReport,Long> {
    @Query(value = "SELECT * FROM TBL_REPORT WHERE REPORT_ID = :reportId",nativeQuery = true)
    TblReport getReport(long reportId);
}
