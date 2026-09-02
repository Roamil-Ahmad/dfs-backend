package com.dfs.app.repo;

import com.dfs.app.model.TblAccountUpgrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblAccountUpgradeRepo extends JpaRepository<TblAccountUpgrade,Long> {
    TblAccountUpgrade findByTblAccountAccountIdAndTblAccountLevelAccountLevelId(long accountId,long levelId);


    @Query(value = "SELECT * FROM TBL_ACCOUNT_UPGRADE tau INNER JOIN TBL_ACCOUNT ta ON TA.ACCOUNT_ID =TAU.ACCOUNT_ID \n" +
            "INNER JOIN TBL_ACCOUNT_LEVEL tal ON TAL.ACCOUNT_LEVEL_ID =TAU.ACCOUNT_LEVEL_ID\n" +
            "WHERE ta.ACCOUNT_NO =:accountNo AND TAL.ACCOUNT_LEVEL_CODE =:accountLevelCode",nativeQuery = true)
    TblAccountUpgrade findByAccountNoAndAccountLevelCode(String accountNo,String accountLevelCode);
}
