package com.dfs.app.repo;

import com.dfs.app.model.LkpAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpAccountTypeRepo extends JpaRepository<LkpAccountType,Long> {
    LkpAccountType findByAccountTypeCode(String code);
}
