package com.wallet.transaction.repo;

import com.wallet.transaction.model.TblAppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblAppUserRepo extends JpaRepository<TblAppUser, Long> {


    @Query(value = "SELECT CL.CUSTOMER_ALL_ID FROM TBL_CUSTOMER_ALL CL WHERE CL.MOBILE_NO=:mobileNumber AND CL.IMEI_NO=:imeiNo", nativeQuery = true)
    Long validateDeviceToken(String mobileNumber, String imeiNo);

    @Query(value = "SELECT A.* FROM TBL_APP_USER A \n" +
            "INNER JOIN TBL_CUSTOMER C ON C.CUSTOMER_ID=A.CUSTOMER_ID \n" +
            "INNER JOIN TBL_CUSTOMER_ALL CA ON C.CUSTOMER_ALL_ID=CA.CUSTOMER_ALL_ID\n" +
            "WHERE CA.MOBILE_NO=:mobileNumber AND C.IS_ACTIVE='Y'",nativeQuery = true)
    TblAppUser findByMobileNumber(String mobileNumber);


    @Query(value = "SELECT A.* FROM TBL_APP_USER a\n" +
            "            INNER JOIN TBL_DEVICE_INFO tdi ON tdi.MOBILE_NO =a.MOBILE_NO \n" +
            "            WHERE a.MOBILE_NO=:mobileNumber",nativeQuery = true)
    TblAppUser getAppUserByMobileNo(String mobileNumber);

    @Query(value = "SELECT * FROM TBL_APP_USER WHERE CUSTOMER_ID=:customerId", nativeQuery = true)
    TblAppUser findByCustomerId(long customerId);

    /**
     * The agent's primary app user. A corporate agent owns one app user per partner, so keyed on
     * the agent alone this matched several rows and threw - which is what broke fundsTransferLocal
     * after the transfer had already been posted. Ordered so the agent's own login, written first,
     * is the one returned; an agent without partners is unaffected.
     */
    @Query(value = "SELECT * FROM TBL_APP_USER WHERE AGENT_ID=:agentId \n" +
            "            ORDER BY APP_USER_ID FETCH FIRST 1 ROW ONLY", nativeQuery = true)
    TblAppUser findByAgentId(long agentId);
}
