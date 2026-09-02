package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpAccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpAccountStatusRepo extends JpaRepository<LkpAccountStatus,Long> {
    LkpAccountStatus findByAccountStatusCode(String code);
}
