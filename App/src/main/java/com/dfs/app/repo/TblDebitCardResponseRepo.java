package com.dfs.app.repo;

import com.dfs.app.model.TblDebitCardResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblDebitCardResponseRepo extends JpaRepository<TblDebitCardResponse,Long> {
    TblDebitCardResponse findByTblDebitCardRequestDebitCardRequestId(long debitCardRequestId);
}
