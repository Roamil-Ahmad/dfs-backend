package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.TblSmsMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblSmsMessageRepo extends JpaRepository<TblSmsMessage, Long> {
}
