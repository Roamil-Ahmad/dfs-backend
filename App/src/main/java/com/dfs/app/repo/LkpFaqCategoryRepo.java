package com.dfs.app.repo;

import com.dfs.app.model.LkpFaqCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpFaqCategoryRepo extends JpaRepository<LkpFaqCategory,Long> {
    List<LkpFaqCategory> findByIsActive(String isActive);
}
