package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpAccountPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LKpAccountPurposeRepo extends JpaRepository<LkpAccountPurpose,Long> {
    List<LkpAccountPurpose> findAllByIsActive(String isActive);

    LkpAccountPurpose findByAccountPurposeCodeAndIsActive(String purposeOfAccountCode, String yes);
}
