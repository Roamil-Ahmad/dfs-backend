package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpSourceOfIncome;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpSourceOfIncomeRepo extends JpaRepository<LkpSourceOfIncome,Long> {
    List<LkpSourceOfIncome> findAllByIsActive(String isActive);

    LkpSourceOfIncome findBySourceOfIncomeCodeAndIsActive(String sourceOfIncomeCode, String yes);
}
