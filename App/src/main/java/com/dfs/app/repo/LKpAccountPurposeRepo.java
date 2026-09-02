package com.dfs.app.repo;

import com.dfs.app.model.LkpAccountPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LKpAccountPurposeRepo extends JpaRepository<LkpAccountPurpose,Long> {
    List<LkpAccountPurpose> findAllByIsActive(String isActive);

    LkpAccountPurpose findByAccountPurposeCodeAndIsActive(String purposeOfAccountCode, String yes);
}
