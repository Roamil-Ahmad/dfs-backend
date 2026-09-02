package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpIssueType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpIssueTypeRepo extends JpaRepository<LkpIssueType,Long> {
    List<LkpIssueType> findByIsActive(String isActive);
}
