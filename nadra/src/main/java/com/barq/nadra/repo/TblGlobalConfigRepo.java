//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.barq.nadra.repo;

import com.barq.nadra.model.TblGlobalConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TblGlobalConfigRepo extends JpaRepository<TblGlobalConfig, Long> {
    @Query(
            value = "SELECT KEY_VALUE FROM TBL_GLOBAL_CONFIG WHERE KEY_NAME = :key ",
            nativeQuery = true
    )
    String findByKeyName(@Param("key") String key);
}
