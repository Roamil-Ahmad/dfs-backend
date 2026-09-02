package com.dfs.app.repo;

import com.dfs.app.model.LkpStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpStatusRepo extends JpaRepository<LkpStatus,Long> {
    LkpStatus findByStatusCodeAndIsActive(String statusCode,String isActive);

}
