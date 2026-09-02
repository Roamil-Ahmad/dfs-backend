package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.TblGlobalConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblGlobalConfigRepo extends JpaRepository<TblGlobalConfig, Long> {

	TblGlobalConfig findByKeyName(String keyName);

}
