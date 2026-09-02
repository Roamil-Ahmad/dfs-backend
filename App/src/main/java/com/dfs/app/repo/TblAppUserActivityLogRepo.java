package com.dfs.app.repo;

import com.dfs.app.model.TblAppUserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblAppUserActivityLogRepo extends JpaRepository<TblAppUserActivityLog,Long> {
}
