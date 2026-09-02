package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblGlobalConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblGlobalConfigRepo extends JpaRepository<TblGlobalConfig,Long> {

    TblGlobalConfig findByKeyName(String keyName);

}
