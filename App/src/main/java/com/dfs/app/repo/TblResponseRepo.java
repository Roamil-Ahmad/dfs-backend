package com.dfs.app.repo;


import com.dfs.app.model.TblResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblResponseRepo extends JpaRepository<TblResponse, Long> {

    @Query(value = "SELECT * FROM TBL_RESPONSE WHERE REQUEST_ID=:requestId",nativeQuery = true)
    TblResponse findByRequestId(long requestId);
}
