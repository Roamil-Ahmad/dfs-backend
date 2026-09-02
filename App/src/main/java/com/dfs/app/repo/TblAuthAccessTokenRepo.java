package com.dfs.app.repo;


import com.dfs.app.model.TblAuthAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface TblAuthAccessTokenRepo extends JpaRepository<TblAuthAccessToken, Long> {
	@Query(value = "SELECT COUNT(*)\n" +
			"  FROM TBL_AUTH_ACCESS_TOKEN N\n" +
			"  LEFT JOIN TBL_APP_USER_LOGIN_HISTORY U ON N.APP_USER_LOGIN_HISTORY_ID = U.APP_USER_LOGIN_HISTORY_ID\n" +
			" WHERE (U.APP_USER_ID = :appUserId OR N.CUSTOMER_ALL_ID = :customerAllId)\n" +
			"   AND SYSDATE BETWEEN N.EFFECTIVE_FROM AND N.EFFECTIVE_TO\n" +
			"   AND N.IS_ACTIVE = 'Y' AND N.ACCESS_TOKEN=:token",nativeQuery = true)
	int getAuthTokenAuthorization(String appUserId,String customerAllId,String token);
	@Transactional
	@Modifying
	@Query(value = "UPDATE TBL_AUTH_ACCESS_TOKEN " +
			"SET EFFECTIVE_TO = SYSDATE + ((SELECT TO_NUMBER(KEY_VALUE) FROM TBL_GLOBAL_CONFIG WHERE KEY_NAME = 'LOGOUT_TIME') / 1440) " +
			"WHERE IS_ACTIVE = 'Y' " +
			"AND (CUSTOMER_ALL_ID = :customerAllId OR APP_USER_LOGIN_HISTORY_ID IN " +
			"    (SELECT APP_USER_LOGIN_HISTORY_ID FROM TBL_APP_USER_LOGIN_HISTORY WHERE APP_USER_ID = :appUserId))",
			nativeQuery = true)
	void updateTokenValidity(String appUserId, String customerAllId);
	@Transactional
	@Modifying
	@Query(value = "UPDATE TBL_AUTH_ACCESS_TOKEN N\n" +
			"SET N.IS_ACTIVE = 'N'\n" +
			"WHERE N.IS_ACTIVE = 'Y'\n" +
			"  AND (N.CUSTOMER_ALL_ID = :customerAllId \n" +
			"       OR N.APP_USER_LOGIN_HISTORY_ID IN (\n" +
			"           SELECT U.APP_USER_LOGIN_HISTORY_ID\n" +
			"           FROM TBL_APP_USER_LOGIN_HISTORY U\n" +
			"           WHERE U.APP_USER_ID = :appUserId\n" +
			"       ))",
			nativeQuery = true)
	void deactivateTokens(String appUserId, String customerAllId);
@Query(value = "SELECT * FROM TBL_AUTH_ACCESS_TOKEN taat WHERE CUSTOMER_ALL_ID =:id AND IS_ACTIVE ='Y' ORDER BY taat.CREATEDATE DESC\n" +
		"FETCH FIRST ROW ONLY",nativeQuery = true)
	TblAuthAccessToken findByCustomerAllIdAndIsActiveY(long id);
@Query(value = "SELECT taat.* FROM TBL_AUTH_ACCESS_TOKEN taat INNER JOIN TBL_APP_USER_LOGIN_HISTORY taulh ON TAAT.APP_USER_LOGIN_HISTORY_ID =TAULH.APP_USER_LOGIN_HISTORY_ID\n" +
		"INNER JOIN TBL_APP_USER tau ON TAU.APP_USER_ID =TAULH.APP_USER_ID WHERE TAULH.APP_USER_LOGIN_HISTORY_ID =:id AND TAAT.IS_ACTIVE ='Y'",nativeQuery = true)
	TblAuthAccessToken findByAppUserLoginHistoryIdAndIsActiveY(long id);
	@Query(value = "SELECT taat.* FROM TBL_AUTH_ACCESS_TOKEN taat \n" +
			"INNER JOIN TBL_APP_USER_LOGIN_HISTORY taulh ON TAAT.APP_USER_LOGIN_HISTORY_ID =TAULH.APP_USER_LOGIN_HISTORY_ID\n" +
			"INNER JOIN TBL_APP_USER tau ON TAU.APP_USER_ID =TAULH.APP_USER_ID \n" +
			"INNER JOIN TBL_CUSTOMER tc ON tc.CUSTOMER_ID =tau.CUSTOMER_ID \n" +
			"INNER JOIN TBL_ACCOUNT a ON a.CUSTOMER_ID =tc.CUSTOMER_ID \n" +
			"WHERE a.ACCOUNT_NO= :accountNo\n" +
			"AND TAAT.IS_ACTIVE ='Y' ORDER BY taat.CREATEDATE DESC FETCH FIRST ROW ONLY",nativeQuery = true)
	TblAuthAccessToken findByAccountNoAndIsActiveY(String accountNo);
}
