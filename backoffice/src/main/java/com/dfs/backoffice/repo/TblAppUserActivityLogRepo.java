package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.TblAppUserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblAppUserActivityLogRepo extends JpaRepository<TblAppUserActivityLog,Long> {
}
