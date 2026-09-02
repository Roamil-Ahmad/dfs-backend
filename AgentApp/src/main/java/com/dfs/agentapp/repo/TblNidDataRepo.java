package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblNidData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * Read-only access to the CNIC record the nadra service stored. There is no write method by
 * design: TBL_NID_DATA belongs to that service.
 */
@Repository
public interface TblNidDataRepo extends JpaRepository<TblNidData, BigDecimal> {

    /**
     * Most recent record for a CNIC. A CNIC can be verified more than once, and only the latest
     * values should drive the challenge.
     */
    @Query(value = "SELECT * FROM TBL_NID_DATA WHERE NID_NO = :nidNo ORDER BY CREATEDATE DESC NULLS LAST FETCH FIRST 1 ROWS ONLY",
            nativeQuery = true)
    TblNidData findLatestByNidNo(@Param("nidNo") Long nidNo);
}
