package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.TblUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblUserRoleRepo extends JpaRepository<TblUserRole,Long> {
    TblUserRole findByTblUserUserId(long longValue);
}
