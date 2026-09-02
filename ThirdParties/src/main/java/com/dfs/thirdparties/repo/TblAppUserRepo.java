package com.dfs.thirdparties.repo;


import com.dfs.thirdparties.model.TblAppUser;

import com.dfs.thirdparties.model.TblCustomerAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TblAppUserRepo extends JpaRepository<TblAppUser,Long> {

    TblAppUser findByUsername(String username);

    @Query(value = "SELECT R.ROLE_CODE FROM TBL_APP_USER AU INNER JOIN TBL_USER U on U.USER_ID=AU.USER_ID\n" +
            "INNER JOIN TBL_USER_ROLE UR ON U.USER_ID=UR.USER_ID\n" +
            "INNER JOIN TBL_ROLE R ON R.ROLE_ID=UR.ROLE_ID WHERE AU.USERNAME=:username",nativeQuery =true )
    String getRoleCodeByAppUserUsername(String username);

    @Query(value = "SELECT * FROM TBL_APP_USER WHERE USERNAME=:username AND PASSWORD=:password",nativeQuery = true)
    TblAppUser findByUsernameAndPassword(String username, String password);

    @Query(value = "SELECT CL.CUSTOMER_ALL_ID FROM TBL_CUSTOMER_ALL CL INNER JOIN TBL_CUSTOMER C ON CL.CUSTOMER_ALL_ID=C.CUSTOMER_ALL_ID  WHERE C.CUSTOMER_ID=:customerId",nativeQuery = true)
    Long getCustomerAllIdByCustomerId(Long customerId);

    @Query(value = "SELECT CL.CUSTOMER_ALL_ID FROM TBL_CUSTOMER_ALL CL WHERE CL.MOBILE_NO=:mobileNumber AND CL.IMEI_NO=:imeiNo",nativeQuery = true)
    Long validateDeviceToken(String mobileNumber,String imeiNo);

    @Query(value = "SELECT CUSTOMER_ALL_ID FROM TBL_CUSTOMER_ALL WHERE MOBILE_NO=:mobileNumber",nativeQuery = true)
    Long getCustomerAllIdByMobileNumber(String mobileNumber);
//    @Query(value = "SELECT CUSTOMER_ID FROM TBL_CUSTOMER WHERE EMAIL=:email",nativeQuery = true)
//    Long getCustomerIdByEmail(String email);
    @Query(value = "SELECT CUSTOMER_ID FROM TBL_CUSTOMER WHERE CUSTOMER_ALL_ID=:customerAllId",nativeQuery = true)
    Long getCustomerIdByCustomerAllId(Long customerAllId);
    @Query(value = "SELECT tau.* FROM TBL_AGENT ta INNER JOIN TBL_APP_USER tau  ON ta.AGENT_ID =TAU.AGENT_ID WHERE ta.MOBILE =:mobileNumber",nativeQuery = true)
    TblAppUser findByAgentMobileNumber(String mobileNumber);

    @Query(value = "SELECT AGENT_ID FROM TBL_AGENT WHERE CUSTOMER_ALL_ID=:customerAllId",nativeQuery = true)
    Long getAgentIdByCustomerAllId(long customerAllId);
//    @Query(value = "SELECT tau.* FROM TBL_AGENT ta INNER JOIN TBL_APP_USER tau  ON ta.AGENT_ID =TAU.AGENT_ID WHERE ta.EMAIL =:email",nativeQuery = true)
//    TblAppUser findByAgentEmail(String email);

}
