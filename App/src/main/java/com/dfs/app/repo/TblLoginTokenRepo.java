package com.dfs.app.repo;

import com.dfs.app.model.TblLoginToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblLoginTokenRepo extends JpaRepository<TblLoginToken,Long> {

    @Query(value = "SELECT COUNT(*) FROM TBL_LOGIN_TOKEN N WHERE N.APP_USER_ID = :appUserId AND" +
            " SYSDATE BETWEEN N.EFFECTIVE_FROM AND N.EFFECTIVE_TO\n" +
            "AND N.IS_ACTIVE = 'Y' AND N.LOGIN_TOKEN=:token",nativeQuery = true)
    int authorizeLoginToken(long appUserId,String token);

    @Query(value = "SELECT * FROM TBL_LOGIN_TOKEN N WHERE N.APP_USER_ID = :appUserId AND N.IS_ACTIVE=:active",nativeQuery = true)
    TblLoginToken findByAppUserIdAndIsActive(long appUserId, String active);

    @Query(value = "SELECT * FROM TBL_LOGIN_TOKEN N \n" +
            "INNER JOIN TBL_APP_USER tau ON TAU.APP_USER_ID =N.APP_USER_ID \n" +
            "INNER JOIN TBL_CUSTOMER tc ON tc.CUSTOMER_ID =tau.CUSTOMER_ID \n" +
            "INNER JOIN TBL_ACCOUNT a ON a.CUSTOMER_ID =tc.CUSTOMER_ID \n" +
            "WHERE a.ACCOUNT_NO= :accountNo\n" +
            "AND N.IS_ACTIVE =:yes ORDER BY N.CREATEDATE DESC FETCH FIRST ROW ONLY",nativeQuery = true)
    TblLoginToken findByAccountNoAndIsActive(String accountNo, String yes);
}
