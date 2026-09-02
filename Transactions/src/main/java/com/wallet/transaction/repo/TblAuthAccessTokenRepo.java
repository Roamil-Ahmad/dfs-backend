package com.wallet.transaction.repo;


import com.wallet.transaction.model.TblAuthAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface TblAuthAccessTokenRepo extends JpaRepository<TblAuthAccessToken, Long> {
	@Query(value = "SELECT COUNT(*)\n" +
			"FROM TBL_AUTH_ACCESS_TOKEN N\n" +
			"INNER JOIN TBL_APP_USER_LOGIN_HISTORY U \n" +
			"    ON N.APP_USER_LOGIN_HISTORY_ID = U.APP_USER_LOGIN_HISTORY_ID\n" +
			"WHERE (U.APP_USER_ID =:appUserId OR N.CUSTOMER_ALL_ID =:customerAllId)\n" +
			"  AND SYSDATE BETWEEN N.EFFECTIVE_FROM AND N.EFFECTIVE_TO\n" +
			"  AND N.IS_ACTIVE = 'Y'",nativeQuery = true)
	int getAuthTokenAuthorization(String appUserId, String customerAllId);


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
}
