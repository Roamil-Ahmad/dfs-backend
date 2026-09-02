package com.dfs.app.repo;

import com.dfs.app.model.LkpAccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpAccountStatusRepo extends JpaRepository<LkpAccountStatus,Long> {
    LkpAccountStatus findByAccountStatusCode(String code);
}
