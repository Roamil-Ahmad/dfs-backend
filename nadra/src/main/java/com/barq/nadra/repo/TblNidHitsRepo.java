package com.barq.nadra.repo;

import com.barq.nadra.model.TblNidHits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TblNidHitsRepo extends JpaRepository<TblNidHits,Long> {

    @Query(value = "SELECT SESSION_ID, TRANSACTION_ID\n" +
            "  FROM TBL_NID_HITS\n" +
            " WHERE NID_NO = :nidNo  \n" +
            " AND RESPONSE NOT IN ('100','119')\n" +
            " GROUP BY SESSION_ID, TRANSACTION_ID\n" +
            "HAVING SYSDATE - (5/24)/60 <= MAX(CREATEDATE)",nativeQuery = true)
    Object getSessionId(@Param("nidNo") String nidNo);
}
