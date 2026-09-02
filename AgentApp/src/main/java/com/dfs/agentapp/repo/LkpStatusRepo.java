package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpStatusRepo extends JpaRepository<LkpStatus,Long> {
    LkpStatus findByStatusCodeAndIsActive(String statusCode, String isActive);

}
