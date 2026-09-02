package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.TblAgentClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblAgentClassRepo extends JpaRepository<TblAgentClass,Long> {
    List<TblAgentClass> findByIsActive(String yes);
}
