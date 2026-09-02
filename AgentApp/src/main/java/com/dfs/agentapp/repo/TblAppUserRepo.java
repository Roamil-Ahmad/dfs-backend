package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblAppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TblAppUserRepo extends JpaRepository<TblAppUser, Long> {

    TblAppUser findByUsername(String username);

    @Query(value = "SELECT CL.CUSTOMER_ALL_ID FROM TBL_CUSTOMER_ALL CL WHERE CL.MOBILE_NO=:mobileNumber AND CL.IMEI_NO=:imeiNo", nativeQuery = true)
    Long validateDeviceToken(String mobileNumber, String imeiNo);

    @Query(value = "SELECT AU.*\n" +
            "              FROM TBL_APP_USER AU \n" +
            "             INNER JOIN TBL_ACCOUNT ta ON AU.AGENT_ID = TA.AGENT_ID\n" +
            "             WHERE AU.USERNAME=:username AND AU.PASSWORD=:password\n" +
            "               AND TA.REGISTRATION_TYPE_ID IN (2,5)", nativeQuery = true)
    TblAppUser findByUsernameAndPassword(String username, String password);

    @Query(value = "SELECT A.* FROM TBL_APP_USER A \n" +
            "            INNER JOIN TBL_AGENT AG ON AG.AGENT_ID=A.AGENT_ID \n" +
            "            WHERE AG.MOBILE=:mobileNumber \n" +
            "            ORDER BY A.APP_USER_ID FETCH FIRST 1 ROW ONLY", nativeQuery = true)
    TblAppUser findByMobileNumber(String mobileNumber);

    @Query(value = "SELECT A.* FROM TBL_APP_USER a\n" +
            "            INNER JOIN TBL_DEVICE_INFO tdi ON tdi.MOBILE_NO =a.MOBILE_NO \n" +
            "            WHERE a.MOBILE_NO=:mobileNumber", nativeQuery = true)
    TblAppUser getAppUserByMobileNo(String mobileNumber);

    /**
     * The agent's primary app user: the mobile login created during KYC.
     *
     * <p>A corporate agent owns more than one row - one extra per partner, keyed on the partner's
     * email. The primary row is always written first and therefore holds the lowest APP_USER_ID,
     * which is what this returns. The ordering and the limit are what keep it single valued: the
     * derived finder threw as soon as a second row existed, which would have broken device
     * registration, login history and the maker-checker lookup. For an agent with no partners the
     * result is exactly the row it returned before.</p>
     */
    @Query(value = "SELECT * FROM TBL_APP_USER WHERE AGENT_ID = :id ORDER BY APP_USER_ID FETCH FIRST 1 ROW ONLY",
            nativeQuery = true)
    TblAppUser findByAgentId(@Param("id") Long id);

    @Query(value = "SELECT A.* FROM TBL_APP_USER A \n" +
            "            INNER JOIN TBL_AGENT C ON C.AGENT_ID=A.AGENT_ID \n" +
            "            INNER JOIN TBL_ACCOUNT T ON T.AGENT_ID=C.AGENT_ID\n" +
            "            WHERE T.ACCOUNT_NO=:mobileNumber AND C.IS_ACTIVE='Y' \n" +
            "            ORDER BY A.APP_USER_ID FETCH FIRST 1 ROW ONLY", nativeQuery = true)
    TblAppUser findByAccountNo(String mobileNumber);


    @Query(value = "SELECT * FROM TBL_APP_USER WHERE CUSTOMER_ID=:customerId", nativeQuery = true)
    TblAppUser findByCustomerId(long customerId);

    @Query(value = "SELECT A.* FROM TBL_APP_USER A \n" +
            "            INNER JOIN TBL_CUSTOMER C ON C.CUSTOMER_ID=A.CUSTOMER_ID \n" +
            "            INNER JOIN TBL_ACCOUNT T ON T.CUSTOMER_ID=C.CUSTOMER_ID\n" +
            "            WHERE T.ACCOUNT_NO=:mobileNumber AND C.IS_ACTIVE='Y'", nativeQuery = true)
    TblAppUser findByCustomerAccountNo(String mobileNumber);
}
