package com.dfs.app.repo;

import com.dfs.app.model.TblGlobalConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblGlobalConfigRepo extends JpaRepository<TblGlobalConfig,Long> {

    TblGlobalConfig findByKeyName(String keyName);

}
