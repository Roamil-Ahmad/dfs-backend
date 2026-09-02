package com.dfs.app.repo;

import com.dfs.app.model.LkpCmsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpCmsCategoryRepo extends JpaRepository<LkpCmsCategory, Long> {
    List<LkpCmsCategory> findByIsActive(String isActive);
}
