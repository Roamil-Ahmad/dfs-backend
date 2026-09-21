package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblDebitCardResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblDebitCardResponseRepo extends JpaRepository<TblDebitCardResponse,Long> {
    TblDebitCardResponse findByTblDebitCardRequestDebitCardRequestId(long debitCardRequestId);
}
