package com.dfs.app.repo;

import com.dfs.app.model.TblAppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblAppUserRepo extends JpaRepository<TblAppUser, Long> {

    TblAppUser findByUsername(String username);

    @Query(value = "SELECT CL.CUSTOMER_ALL_ID FROM TBL_CUSTOMER_ALL CL WHERE CL.MOBILE_NO=:mobileNumber AND CL.IMEI_NO=:imeiNo", nativeQuery = true)
    Long validateDeviceToken(String mobileNumber, String imeiNo);

    @Query(value = "SELECT * \n" +
            "  FROM TBL_APP_USER AU \n" +
            " INNER JOIN TBL_ACCOUNT ta ON AU.CUSTOMER_ID = TA.CUSTOMER_ID\n" +
            "INNER JOIN LKP_REGISTRATION_TYPE R ON TA.REGISTRATION_TYPE_ID =R.REGISTRATION_TYPE_ID\n" +
            " WHERE AU.USERNAME=:username AND AU.PASSWORD=:password\n" +
            "AND R.REGISTRATION_TYPE_CODE= 'C'",nativeQuery = true)
    TblAppUser findByUsernameAndPassword(String username,String password);

    @Query(value = "SELECT * \n" +
            "FROM TBL_APP_USER AU\n" +
            "INNER JOIN TBL_ACCOUNT TA ON AU.CUSTOMER_ID = TA.CUSTOMER_ID\n" +
            "INNER JOIN LKP_REGISTRATION_TYPE R ON TA.REGISTRATION_TYPE_ID =R.REGISTRATION_TYPE_ID\n" +
            "WHERE AU.USERNAME = :username\n" +
            "AND R.REGISTRATION_TYPE_CODE= 'C'",nativeQuery = true)
    TblAppUser findAppUserByUserName(String username);

    @Query(value = "SELECT CL.CUSTOMER_ALL_ID FROM TBL_CUSTOMER_ALL CL INNER JOIN TBL_CUSTOMER C ON CL.CUSTOMER_ALL_ID=C.CUSTOMER_ALL_ID  WHERE C.CUSTOMER_ID=:customerId",nativeQuery = true)
    Long getCustomerAllIdByCustomerId(Long customerId);

    @Query(value = "SELECT * FROM TBL_APP_USER WHERE CUSTOMER_ID=:customerId", nativeQuery = true)
    TblAppUser findByCustomerId(long customerId);

    @Query(value = "SELECT A.* FROM TBL_APP_USER A \n" +
            "            INNER JOIN TBL_CUSTOMER C ON C.CUSTOMER_ID=A.CUSTOMER_ID \n" +
            "            INNER JOIN TBL_ACCOUNT T ON T.CUSTOMER_ID=C.CUSTOMER_ID\n" +
            "            WHERE T.ACCOUNT_NO=:mobileNumber AND C.IS_ACTIVE='Y'", nativeQuery = true)
    TblAppUser findByAccountNo(String mobileNumber);

    @Query(value = "SELECT A.* FROM TBL_APP_USER a\n" +
            "            INNER JOIN TBL_DEVICE_INFO tdi ON tdi.MOBILE_NO =a.MOBILE_NO \n" +
            "            WHERE tdi.MOBILE_NO=:mobileNumber", nativeQuery = true)
    TblAppUser getAppUserByMobileNo(String mobileNumber);


    @Query(value = "SELECT tau.* FROM TBL_APP_USER tau INNER JOIN TBL_CUSTOMER tc ON tc.CUSTOMER_ID =TAU.CUSTOMER_ID \n" +
            "INNER JOIN TBL_CUSTOMER_ALL tca ON tca.CUSTOMER_ALL_ID =tc.CUSTOMER_ALL_ID WHERE tca.IMEI_NO =:imieNo", nativeQuery = true)
    TblAppUser findByImeiNumber(String imieNo);


}
