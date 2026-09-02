package com.dfs.thirdparties.repo;

import com.dfs.thirdparties.model.TblGlobalConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblGlobalConfigRepo extends JpaRepository<TblGlobalConfig,Long> {

    TblGlobalConfig findByKeyName(String keyName);

}
