package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpAccountTypeRepo extends JpaRepository<LkpAccountType,Long> {
    LkpAccountType findByAccountTypeCode(String code);
}
