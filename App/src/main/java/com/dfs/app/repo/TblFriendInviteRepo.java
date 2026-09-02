package com.dfs.app.repo;

import com.dfs.app.model.TblFriendInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TblFriendInviteRepo extends JpaRepository<TblFriendInvite, Long> {

    @Query(value = "SELECT COUNT(1) FROM TBL_FRIEND_INVITE " + "WHERE MOBILE_NO = :mobileNo " + "AND STATUS = 'P'", nativeQuery = true)
    int countPendingInvitesByMobile(@Param("mobileNo") String mobileNo);

    @Query(value = "SELECT COUNT(1) FROM TBL_ACCOUNT WHERE ACCOUNT_NO = :mobileNo", nativeQuery = true)
    int countByAccountNo(@Param("mobileNo") String mobileNo);

    @Query(value = " SELECT " +
            "            U.APP_USER_ID," +
            "            NVL(T.TOTAL_INVITES, 0)," +
            "            NVL(P.PENDING_INVITES, 0)," +
            "            NVL(C.COMPLETE_INVITES, 0)" +
            "        FROM " +
            "            TBL_APP_USER U " +
            "        LEFT JOIN (" +
            "            SELECT COUNT(1) TOTAL_INVITES, U.APP_USER_ID " +
            "            FROM TBL_FRIEND_INVITE F" +
            "            INNER JOIN TBL_ACCOUNT A ON F.INVITOR_ACCOUNT_ID = A.ACCOUNT_ID" +
            "            INNER JOIN TBL_APP_USER U ON A.CUSTOMER_ID = U.CUSTOMER_ID " +
            "            GROUP BY U.APP_USER_ID" +
            "        ) T ON U.APP_USER_ID = T.APP_USER_ID" +
            "        LEFT JOIN (" +
            "            SELECT COUNT(1) PENDING_INVITES, U.APP_USER_ID " +
            "            FROM TBL_FRIEND_INVITE F" +
            "            INNER JOIN TBL_ACCOUNT A ON F.INVITOR_ACCOUNT_ID = A.ACCOUNT_ID" +
            "            INNER JOIN TBL_APP_USER U ON A.CUSTOMER_ID = U.CUSTOMER_ID " +
            "            WHERE F.STATUS = 'P'" +
            "            GROUP BY U.APP_USER_ID" +
            "        ) P ON U.APP_USER_ID = P.APP_USER_ID" +
            "        LEFT JOIN (" +
            "            SELECT COUNT(1) COMPLETE_INVITES, U.APP_USER_ID " +
            "            FROM TBL_FRIEND_INVITE F" +
            "            INNER JOIN TBL_ACCOUNT A ON F.INVITOR_ACCOUNT_ID = A.ACCOUNT_ID" +
            "            INNER JOIN TBL_APP_USER U ON A.CUSTOMER_ID = U.CUSTOMER_ID " +
            "            WHERE F.STATUS = 'C'" +
            "            GROUP BY U.APP_USER_ID" +
            "        ) C ON U.APP_USER_ID = C.APP_USER_ID" +
            "        WHERE U.APP_USER_ID = :appUserId", nativeQuery = true)
    Object getInviteStatsByAppUserId(@Param("appUserId") int appUserId);
}
