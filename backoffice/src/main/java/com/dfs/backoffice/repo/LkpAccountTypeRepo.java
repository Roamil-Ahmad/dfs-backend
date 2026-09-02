/*
Author Name: romail.ahmed

Project Name: backoffice

Package Name: com.dfs.backoffice.repo

Class Name: LkpAccountTypeRepo

Date and Time:1/25/2025 7:53 PM

Version:1.0
*/
package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.LkpAccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface LkpAccountTypeRepo extends JpaRepository<LkpAccountType, Long> {

    @Query("SELECT COALESCE(MAX(l.sortSeq), 0) FROM LkpAccountType l")
    BigDecimal findMaxSortSeq();

    List<LkpAccountType> findByAccountType(String glType);

    LkpAccountType findByAccountTypeCode(String accountTypeAgent);
}
