package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.TblAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblAccountRepo extends JpaRepository<TblAccount, Long> {

    @Query(value = "SELECT * FROM TBL_ACCOUNT WHERE ACCOUNT_NO =:accountNo", nativeQuery = true)
    TblAccount getAccountAgainstAccountNo(String accountNo);

    @Query(value = "SELECT * FROM TBL_ACCOUNT WHERE ACCOUNT_NO = :agentMobileNo OR MOBILE_NO = :agentMobileNo", nativeQuery = true)
    TblAccount checkAccountExistence(String agentMobileNo);

    @Query(value = "SELECT A.* FROM TBL_ACCOUNT A INNER JOIN TBL_ACCOUNT_LEVEL AL on AL.ACCOUNT_LEVEL_ID=A.ACCOUNT_LEVEL_ID\n" +
            "INNER JOIN TBL_CUSTOMER C ON A.CUSTOMER_ID=C.CUSTOMER_ID \n" +
            "INNER JOIN TBL_CUSTOMER_ALL CL ON C.CUSTOMER_ALL_ID=CL.CUSTOMER_ALL_ID\n" +
            "WHERE AL.ACCOUNT_LEVEL_CODE=:s AND CL.MOBILE_NO=:mobileNumber OR C.NID_NO=:nidNumber", nativeQuery = true)
    TblAccount findByMobileNoOrCnicAndAccountLevelCode(String mobileNumber, String nidNumber, String s);
}
