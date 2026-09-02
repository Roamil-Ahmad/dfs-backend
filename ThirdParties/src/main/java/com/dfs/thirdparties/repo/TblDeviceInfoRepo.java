package com.dfs.thirdparties.repo;


import com.dfs.thirdparties.model.TblDeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblDeviceInfoRepo extends JpaRepository<TblDeviceInfo,Long> {

    TblDeviceInfo findByMobileNo(String mobileNumber);

    @Query(value = "SELECT tdi.DEVICE_MODEL FROM TBL_DEVICE_INFO tdi WHERE tdi.MOBILE_NO =:mobileNumber",nativeQuery = true)
    String getDeviceInfo(String mobileNumber);

    @Query(value = "SELECT D.* FROM TBL_APP_USER A \n" +
            "            INNER JOIN TBL_CUSTOMER C ON C.CUSTOMER_ID=A.CUSTOMER_ID \n" +
            "            INNER JOIN TBL_ACCOUNT T ON T.CUSTOMER_ID=C.CUSTOMER_ID\n" +
            "            INNER JOIN TBL_DEVICE_INFO D on D.APP_USER_ID=A.APP_USER_ID\n" +
            "            WHERE T.ACCOUNT_NO=:accountNo AND C.IS_ACTIVE='Y'",nativeQuery = true)
    TblDeviceInfo findByAccountNo(String accountNo);

    @Query(value = "SELECT D.* FROM TBL_APP_USER A \n" +
            "            INNER JOIN TBL_AGENT C ON C.AGENT_ID=A.AGENT_ID \n" +
            "            INNER JOIN TBL_ACCOUNT T ON T.AGENT_ID=C.AGENT_ID\n" +
            "            INNER JOIN TBL_DEVICE_INFO D on D.APP_USER_ID=A.APP_USER_ID\n" +
            "            WHERE T.ACCOUNT_NO=:accountNo AND C.IS_ACTIVE='Y'",nativeQuery = true)
    TblDeviceInfo findByAccountNoAgent(String accountNo);
}
