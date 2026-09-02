package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.model.TblAppUserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface TblAppUserLoginHistoryRepo extends JpaRepository<TblAppUserLoginHistory, Long> {

    @Modifying
    @Query(value = "UPDATE TblAppUserLoginHistory SET logoutDate = :logoutDate WHERE appUserLoginHistoryId = :userLoginHistoryId ")
    int logoutUser(long userLoginHistoryId, Date logoutDate);

    @Query(value = "SELECT * FROM TBL_APP_USER_LOGIN_HISTORY A\n" +
            "            WHERE A.APP_USER_ID =:appUserId\n" +
            "            ORDER BY A.LOGIN_DATE DESC\n" +
            "            FETCH FIRST ROW ONLY", nativeQuery = true)
    TblAppUserLoginHistory getLatestAppUserByAppUserId(long appUserId);
}
