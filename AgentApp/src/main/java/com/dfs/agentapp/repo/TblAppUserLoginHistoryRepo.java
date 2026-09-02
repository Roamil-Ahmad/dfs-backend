package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblAppUserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblAppUserLoginHistoryRepo extends JpaRepository<TblAppUserLoginHistory,Long> {

    @Query(value = "SELECT * FROM TBL_APP_USER_LOGIN_HISTORY A\n" +
            "            WHERE A.APP_USER_ID =:appUserId\n" +
            "            ORDER BY A.LOGIN_DATE DESC\n" +
            "            FETCH FIRST ROW ONLY",nativeQuery = true)
    TblAppUserLoginHistory getLatestAppUserByAppUserId(long appUserId);
}
