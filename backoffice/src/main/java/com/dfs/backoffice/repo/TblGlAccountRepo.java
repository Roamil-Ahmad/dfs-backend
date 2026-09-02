package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.TblGlAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblGlAccountRepo extends JpaRepository<TblGlAccount,Long> {

    @Query(value = "SELECT * FROM TBL_GL_ACCOUNT WHERE PAR_REF_ID IS NOT NULL ORDER BY PAR_REF_ID", nativeQuery = true)
    List<TblGlAccount> lovGlChildtAccount();

    @Query(value = "SELECT * FROM TBL_GL_ACCOUNT WHERE PAR_REF_ID = 4", nativeQuery = true)
    List<TblGlAccount> lovGlAccountLiability();

    @Query(value = "SELECT * FROM TBL_GL_ACCOUNT WHERE PAR_REF_ID = :glAccountId order by gl_account_id desc", nativeQuery = true)
    List<TblGlAccount> getChildGlAgainstParent(long glAccountId);

    @Query(value = "SELECT * FROM TBL_GL_ACCOUNT WHERE PAR_REF_ID IS NULL", nativeQuery = true)
    List<TblGlAccount> getAllParentGeneralLedgers();
}
