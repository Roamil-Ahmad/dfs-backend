package com.dfs.app.repo;

import com.dfs.app.model.TblOfac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblOfacRepo  extends JpaRepository<TblOfac,Long> {
    @Modifying
    @Query(value = "DELETE FROM TBL_OFAC",nativeQuery = true)
    int deleteAllPreviousRecord();

    @Query(value = "SELECT * FROM TBL_OFAC WHERE UPPER(NAME) = UPPER(:fullName)",nativeQuery = true)
    List<TblOfac> findByName(String fullName);
}
