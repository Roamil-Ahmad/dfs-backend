package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpTransDocsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpTransDocsCategoryRepo extends JpaRepository<LkpTransDocsCategory,Long> {
    List<LkpTransDocsCategory> findByIsActive(String isActive);
}
