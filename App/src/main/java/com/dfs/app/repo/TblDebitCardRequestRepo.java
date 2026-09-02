package com.dfs.app.repo;

import com.dfs.app.model.TblDebitCardRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblDebitCardRequestRepo extends JpaRepository<TblDebitCardRequest, Long> {
    TblDebitCardRequest findByTblAccountAccountId(long accountId);
}
