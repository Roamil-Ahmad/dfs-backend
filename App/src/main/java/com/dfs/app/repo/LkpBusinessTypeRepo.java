package com.dfs.app.repo;

import com.dfs.app.model.LkpBusinessType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpBusinessTypeRepo extends JpaRepository<LkpBusinessType,Long> {
    List<LkpBusinessType> findAllByIsActive(String isActive);
}
