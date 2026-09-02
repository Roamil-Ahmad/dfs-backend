package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblDeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TblDeviceInfoRepo extends JpaRepository<TblDeviceInfo,Long> {

    @Query(value = "SELECT tdi.* FROM TBL_DEVICE_INFO tdi \n" +
            "INNER JOIN TBL_AGENT ta ON ta.MOBILE =tdi.MOBILE_NO \n" +
            "AND ta.IS_DEVICE_REGISTERED ='Y' AND tdi.MOBILE_NO =:mobileNumber AND tdi.IMEI_NO =:imieNumber",nativeQuery = true)
    TblDeviceInfo findByMobileNoAndImieRegisteredYes(String mobileNumber,String imieNumber);


    /**
     * The device belonging to one app user - the binding the whole model rests on.
     *
     * <p>Every other finder here reaches a device through the agent or the mobile number, which
     * cannot tell one partner of a corporate agent from another. This one starts at the user, so a
     * device that belongs to somebody else is never a candidate.</p>
     *
     * <p>Ordered newest first and limited to one: nothing in the schema stops a second row being
     * written for a user, and the most recent registration is the binding that should win.</p>
     */
    @Query(value = "SELECT * FROM TBL_DEVICE_INFO WHERE APP_USER_ID = :appUserId \n" +
            "            ORDER BY DEVICE_INFO_ID DESC FETCH FIRST 1 ROW ONLY", nativeQuery = true)
    TblDeviceInfo findByAppUserId(@Param("appUserId") long appUserId);

    TblDeviceInfo findByMobileNo(String mobileNumber);

    @Query(value = "SELECT D.* FROM TBL_APP_USER A \n" +
            "            INNER JOIN TBL_AGENT C ON C.AGENT_ID=A.AGENT_ID \n" +
            "            INNER JOIN TBL_ACCOUNT T ON T.AGENT_ID=C.AGENT_ID\n" +
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
