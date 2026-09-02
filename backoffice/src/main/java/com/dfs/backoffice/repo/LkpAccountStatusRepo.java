package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.LkpAccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpAccountStatusRepo extends JpaRepository<LkpAccountStatus,Long> {
    List<LkpAccountStatus> findByIsActive(String yes);

    LkpAccountStatus findByAccountStatusCode(String accountStatusActive);
}
