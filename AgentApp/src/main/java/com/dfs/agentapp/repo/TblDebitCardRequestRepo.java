package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblDebitCardRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblDebitCardRequestRepo extends JpaRepository<TblDebitCardRequest, Long> {
    TblDebitCardRequest findByTblAccountAccountId(long accountId);
}
