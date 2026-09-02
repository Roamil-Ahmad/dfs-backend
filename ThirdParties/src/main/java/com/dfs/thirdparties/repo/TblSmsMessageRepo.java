package com.dfs.thirdparties.repo;

import com.dfs.thirdparties.model.TblSmsMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblSmsMessageRepo extends JpaRepository<TblSmsMessage,Long> {
}
