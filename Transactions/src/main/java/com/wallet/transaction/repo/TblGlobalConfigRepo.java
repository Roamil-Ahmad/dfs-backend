package com.wallet.transaction.repo;

import com.wallet.transaction.model.TblGlobalConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblGlobalConfigRepo extends JpaRepository<TblGlobalConfig,Long> {

    TblGlobalConfig findByKeyName(String keyName);

}
