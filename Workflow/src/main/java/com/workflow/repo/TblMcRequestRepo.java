package com.workflow.repo;

import com.workflow.modal.TblMcRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TblMcRequestRepo extends JpaRepository<TblMcRequest, Long> {
    TblMcRequest findByMcRequestId(Long limitId);

    @Query(value = " SELECT R.MC_REQUEST_ID, R.FORM_NAME, U.FIRST_NAME||' '||U.LAST_NAME MAKER_NAME, TO_CHAR(R.MAKE_DATE,'DD-MON-YYYY HH24:MI:SS') MAKE_DATE, R.MAKER_COMMENTS, " +
            "       CASE WHEN R.STATUS IN ('S','P') THEN 'Pending for Approval' " +
            "            WHEN R.STATUS IN ('A') THEN 'Approved' END STATUS, R.UPDATEINDEX " +
            "  FROM MC_REQUEST R " +
            " INNER JOIN MC_PENDING_REQUEST P ON R.MC_REQUEST_ID = P.MC_REQUEST_ID " +
            " INNER JOIN UMGT_USER U ON R.MAKER_ID = U.APP_USER_ID " +
            " WHERE P.ACTION_TAKEN = 'N' " +
            "   AND (P.APP_USER_ID = :appUserId " +
            "        OR P.UMGT_ROLE_ID IN (SELECT R.UMGT_ROLE_ID  " +
            "                                FROM UMGT_USER_ROLE R  " +
            "                               INNER JOIN UMGT_USER U ON R.UMGT_USER_ID = U.UMGT_USER_ID " +
            "                               WHERE U.APP_USER_ID =:appUserId))" +
            " ORDER BY R.MC_REQUEST_ID DESC",nativeQuery = true)
    List<Object> getAllMcRequestAgainstAppUserId(@Param("appUserId") String appUserId);

    @Query(value = " SELECT R.MC_REQUEST_ID , R.FORM_NAME, U.FIRST_NAME||' '||U.LAST_NAME MAKER_NAME, TO_CHAR(R.MAKE_DATE,'DD-MON-YYYY HH24:MI:SS') MAKE_DATE, R.MAKER_COMMENTS, " +
            "       'Pending for Approval' STATUS, P.UPDATEINDEX , P.MC_PENDING_REQUEST_ID, R.REF_TABLE_ID, R.VIEW_DETAIL_URL " +
            "  FROM MC_REQUEST R\n" +
            " INNER JOIN MC_PENDING_REQUEST P ON R.MC_REQUEST_ID = P.MC_REQUEST_ID " +
            " INNER JOIN UMGT_USER U ON R.MAKER_ID = U.APP_USER_ID " +
            " WHERE P.ACTION_TAKEN = 'N' " +
            "   AND R.STATUS IN ('S') " +
            "   AND (P.APP_USER_ID =:appUserId " +
            "        OR P.UMGT_ROLE_ID IN (SELECT R.UMGT_ROLE_ID  " +
            "                                FROM UMGT_USER_ROLE R  " +
            "                               INNER JOIN UMGT_USER U ON R.UMGT_USER_ID = U.UMGT_USER_ID " +
            "                               WHERE U.APP_USER_ID =:appUserId)) " +
            " ORDER BY R.MC_REQUEST_ID DESC ",nativeQuery = true)
    List<Object> getPendingMcRequestAgainstAppUserId(@Param("appUserId") String appUserId);


    @Query(value = " SELECT R.FORM_NAME, M.FIRST_NAME||' '||M.LAST_NAME MAKER_NAME, TO_CHAR(R.MAKE_DATE,'DD-MON-YYYY HH24:MI:SS') MAKE_DATE, R.MAKER_COMMENTS, " +
            "       C.FIRST_NAME||' '||C.LAST_NAME CHECKER_NAME, TO_CHAR(A.CHECK_DATE,'DD-MON-YYYY HH24:MI:SS') CHECK_DATE, A.CHECKER_COMMENTS," +
            "       'Approved' STATUS " +
            "  FROM MC_REQUEST R " +
            " INNER JOIN MC_REQUEST_ACTION A ON R.MC_REQUEST_ID = A.MC_REQUEST_ID " +
            " INNER JOIN UMGT_USER M ON R.MAKER_ID = M.APP_USER_ID " +
            " INNER JOIN UMGT_USER C ON A.CHECKER_ID = C.APP_USER_ID " +
            " WHERE A.CHECKER_ID =:appUserId " +
            "   AND R.STATUS = 'A' ",nativeQuery = true)
    List<Object> getApprovedMcRequestAgainstAppUserId(@Param("appUserId") String appUserId);

    @Query(value = "SELECT R.FORM_NAME, M.FIRST_NAME||' '||M.LAST_NAME MAKER_NAME, TO_CHAR(R.MAKE_DATE,'DD-MON-YYYY HH24:MI:SS') MAKE_DATE, R.MAKER_COMMENTS,  " +
            "       C.FIRST_NAME||' '||C.LAST_NAME CHECKER_NAME, TO_CHAR(A.CHECK_DATE,'DD-MON-YYYY HH24:MI:SS') CHECK_DATE, A.CHECKER_COMMENTS, " +
            "       'Rejected' STATUS " +
            "  FROM MC_REQUEST R " +
            " INNER JOIN MC_REQUEST_ACTION A ON R.MC_REQUEST_ID = A.MC_REQUEST_ID " +
            " INNER JOIN UMGT_USER M ON R.MAKER_ID = M.APP_USER_ID " +
            " INNER JOIN UMGT_USER C ON A.CHECKER_ID = C.APP_USER_ID " +
            " WHERE A.CHECKER_ID =:appUserId " +
            "   AND R.STATUS = 'R' ",nativeQuery = true)
    List<Object> getRejectedMcRequestAgainstAppUserId(@Param("appUserId") String appUserId);
}
