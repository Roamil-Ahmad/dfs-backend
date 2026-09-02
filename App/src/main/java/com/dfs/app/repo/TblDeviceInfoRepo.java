package com.dfs.app.repo;

import com.dfs.app.model.TblDeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblDeviceInfoRepo extends JpaRepository<TblDeviceInfo,Long> {

    @Query(value = "SELECT A.* FROM TBL_DEVICE_INFO A WHERE A.MOBILE_NO=:mobileNumber",nativeQuery = true)
    TblDeviceInfo findByMobileNo(String mobileNumber);
@Query(value = "SELECT D.* FROM TBL_APP_USER A \n" +
        "            INNER JOIN TBL_CUSTOMER C ON C.CUSTOMER_ID=A.CUSTOMER_ID \n" +
        "            INNER JOIN TBL_ACCOUNT T ON T.CUSTOMER_ID=C.CUSTOMER_ID\n" +
        "            INNER JOIN TBL_DEVICE_INFO D on D.APP_USER_ID=A.APP_USER_ID\n" +
        "            WHERE T.ACCOUNT_NO=:accountNo AND C.IS_ACTIVE='Y'",nativeQuery = true)
    TblDeviceInfo findByAccountNo(String accountNo);

    @Query(value = "SELECT CASE \n" +
            "           WHEN A.UPDATEINDEX IS NOT NULL AND A.UPDATEINDEX >= 1 THEN 1 \n" +
            "           ELSE 0 \n" +
            "       END AS RESULT\n" +
            "FROM TBL_DEVICE_INFO A \n" +
            "WHERE APP_USER_ID=:appUserId",nativeQuery = true)
    Long checkDeviceUpdateStatusByAppUserId(long appUserId);
}
