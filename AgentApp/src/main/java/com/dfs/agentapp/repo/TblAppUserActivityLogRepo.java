package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblAppUserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblAppUserActivityLogRepo extends JpaRepository<TblAppUserActivityLog,Long> {
}
