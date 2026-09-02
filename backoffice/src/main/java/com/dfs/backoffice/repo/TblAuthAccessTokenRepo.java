package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.TblAuthAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblAuthAccessTokenRepo extends JpaRepository<TblAuthAccessToken, Long> {

    @Query(value = "SELECT taat.* FROM TBL_AUTH_ACCESS_TOKEN taat INNER JOIN TBL_APP_USER_LOGIN_HISTORY taulh ON TAAT.APP_USER_LOGIN_HISTORY_ID =TAULH.APP_USER_LOGIN_HISTORY_ID\n" +
            "INNER JOIN TBL_APP_USER tau ON TAU.APP_USER_ID =TAULH.APP_USER_ID WHERE TAULH.APP_USER_LOGIN_HISTORY_ID =:id AND TAAT.IS_ACTIVE ='Y' ORDER BY taat.CREATEDATE DESC FETCH FIRST ROW ONLY",nativeQuery = true)
    TblAuthAccessToken findByAppUserLoginHistoryIdAndIsActiveY(long id);
}
