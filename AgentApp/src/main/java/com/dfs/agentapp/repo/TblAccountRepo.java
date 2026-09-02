package com.dfs.agentapp.repo;


import com.dfs.agentapp.model.TblAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblAccountRepo extends JpaRepository<TblAccount, Long> {


    @Query(value = "SELECT A.* FROM TBL_ACCOUNT A INNER JOIN TBL_ACCOUNT_LEVEL AL on AL.ACCOUNT_LEVEL_ID=A.ACCOUNT_LEVEL_ID\n" +
            "            INNER JOIN TBL_AGENT C ON A.AGENT_ID =C.AGENT_ID \n" +
            "            WHERE AL.ACCOUNT_LEVEL_CODE=:accountLevelCode AND C.MOBILE=:mobileNumber OR C.NID_NO=:nidNumber", nativeQuery = true)
    TblAccount findByMobileNoOrCnicAndAccountLevelCode(String mobileNumber, String nidNumber, String accountLevelCode);

    @Query(value = "SELECT A.* FROM TBL_ACCOUNT A \n" +
            "            INNER JOIN LKP_ACCOUNT_TYPE AT ON A.ACCOUNT_TYPE_ID=AT.ACCOUNT_TYPE_ID\n" +
            "            INNER JOIN LKP_REGISTRATION_TYPE RT ON RT.REGISTRATION_TYPE_ID=A.REGISTRATION_TYPE_ID\n" +
            "            INNER JOIN TBL_AGENT AG ON AG.AGENT_ID=A.AGENT_ID \n" +
            "            WHERE AG.AGENT_ID=:agentId AND RT.REGISTRATION_TYPE_CODE=:accountRegTypeCode AND AT.ACCOUNT_TYPE_CODE=:accountTypeCode", nativeQuery = true)
    TblAccount findByAgentIdAccountTypeCodeAccountRegType(long agentId, String accountTypeCode, String accountRegTypeCode);

    @Query(value = "SELECT ACCOUNT_ID, DAILY_DR_TOTAL, MONTHLY_DR_TOTAL, YEARLY_DR_TOTAL,DAILY_CR_TOTAL,\n" +
            "MONTHLY_CR_TOTAL, YEARLY_CR_TOTAL,DAILY_DR_REMAINING, MONTHLY_DR_REMAINING,\n" +
            "YEARLY_DR_REMAINING,DAILY_CR_REMAINING, MONTHLY_CR_REMAINING, YEARLY_CR_REMAINING\n" +
            "FROM VW_AVAILABLE_LIMITS WHERE ACCOUNT_ID = :accountId", nativeQuery = true)
    Object viewLimits(long accountId);

    @Query(value = " SELECT * " +
            " FROM VW_MINI_STATEMENT " +
            " WHERE ((FROM_ACCOUNT_ID =:accountId AND FROM_ACCOUNT_TYPE = 'C' AND AMOUNT_TYPE = 'D')  " +
            "    OR (TO_ACCOUNT_ID =:accountId  AND TO_ACCOUNT_TYPE = 'C' AND AMOUNT_TYPE = 'C')) " +
            "   AND TRANS_DATE BETWEEN NVL(TO_DATE(:fromDate,'YYYY-MM-DD HH24:MI:SS'),TRANS_DATE) " +
            "                      AND NVL(TO_DATE(:toDate,'YYYY-MM-DD HH24:MI:SS'),TRANS_DATE)", nativeQuery = true)
    List<Object> miniStatement(long accountId, String fromDate, String toDate);

    TblAccount findByIban(String iban);


    @Query(value = " SELECT * " +
            " FROM VW_MINI_STATEMENT " +
            " WHERE ((FROM_ACCOUNT_ID = :accountId AND FROM_ACCOUNT_TYPE = 'C' AND AMOUNT_TYPE = 'D')  " +
            "       OR (TO_ACCOUNT_ID = :accountId AND TO_ACCOUNT_TYPE = 'C' AND AMOUNT_TYPE = 'C')) " +
            "  AND TRANS_DATE BETWEEN NVL(TO_DATE(:fromDate,'YYYY-MM-DD HH24:MI:SS'), TRANS_DATE) " +
            "                     AND NVL(TO_DATE(:toDate,'YYYY-MM-DD HH24:MI:SS'), TRANS_DATE) " +
            " ORDER BY TRANS_DATE DESC " +
            " FETCH FIRST 9 ROWS ONLY ", nativeQuery = true)
    List<Object> miniStatementFetchNineRecord(long accountId, String fromDate, String toDate);

    @Query(value = "SELECT A.* \n" +
            "FROM TBL_ACCOUNT A\n" +
            "INNER JOIN LKP_ACCOUNT_TYPE AT ON A.ACCOUNT_TYPE_ID = AT.ACCOUNT_TYPE_ID\n" +
            "WHERE A.MOBILE_NO = :mobileNumber \n" +
            "  AND AT.ACCOUNT_TYPE_CODE = :accountTypeCode AND A.IS_ACTIVE ='Y' AND A.ACCOUNT_STATUS_ID <>5", nativeQuery = true)
    TblAccount findByMobileNumberAndAccountTypeCode(String mobileNumber, String accountTypeCode);

    @Query(value = "SELECT A.* FROM TBL_ACCOUNT A INNER JOIN TBL_ACCOUNT_LEVEL AL on AL.ACCOUNT_LEVEL_ID=A.ACCOUNT_LEVEL_ID\n" +
            "            INNER JOIN TBL_CUSTOMER C ON A.CUSTOMER_ID=C.CUSTOMER_ID \n" +
            "            INNER JOIN TBL_CUSTOMER_ALL CL ON C.CUSTOMER_ALL_ID=CL.CUSTOMER_ALL_ID\n" +
            "            WHERE AL.ACCOUNT_LEVEL_CODE=:accountLevelCode AND A.ACCOUNT_NO=:accountNo OR C.NID_NO=:nidNumber", nativeQuery = true)
    TblAccount findByAccountNoOrCnicAndAccountLevelCode(String accountNo, String nidNumber, String accountLevelCode);

    @Query(value = "SELECT A.* FROM TBL_ACCOUNT A INNER JOIN TBL_ACCOUNT_LEVEL AL on AL.ACCOUNT_LEVEL_ID=A.ACCOUNT_LEVEL_ID\n" +
            "            INNER JOIN TBL_AGENT C ON A.AGENT_ID=C.AGENT_ID \n" +
            "            INNER JOIN TBL_CUSTOMER_ALL CL ON C.CUSTOMER_ALL_ID=CL.CUSTOMER_ALL_ID\n" +
            "            WHERE A.ACCOUNT_NO=:accountNo OR C.NID_NO=:nidNumber", nativeQuery = true)
    TblAccount findByAccountNoOrCnicAgent(String accountNo, String nidNumber);

    @Query(value = "  SELECT A.* FROM TBL_ACCOUNT A\n" +
            "            INNER JOIN TBL_AGENT C ON A.AGENT_ID=C.AGENT_ID \n" +
            "            INNER JOIN TBL_CUSTOMER_ALL CL ON C.CUSTOMER_ALL_ID=CL.CUSTOMER_ALL_ID\n" +
            "            WHERE CL.MOBILE_NO=:mobileNumber OR C.NID_NO=:nidNumber AND A.IS_ACTIVE='Y' AND A.IS_ACTIVE='Y'", nativeQuery = true)
    List<TblAccount> findTblAccountsByMobileOrCnic(String mobileNumber, String nidNumber);

    @Query(value = "SELECT A.* FROM TBL_ACCOUNT A INNER JOIN TBL_ACCOUNT_LEVEL AL on AL.ACCOUNT_LEVEL_ID=A.ACCOUNT_LEVEL_ID\n" +
            "INNER JOIN TBL_AGENT C ON A.AGENT_ID=C.AGENT_ID \n" +
            "INNER JOIN TBL_CUSTOMER_ALL CL ON C.CUSTOMER_ALL_ID=CL.CUSTOMER_ALL_ID\n" +
            "WHERE CL.MOBILE_NO=:mobileNumber AND C.NID_NO=:nidNumber", nativeQuery = true)
    TblAccount findByMobileNoAndCnicAndAccountLevelCode(String mobileNumber, String nidNumber);

    TblAccount findByAccountNo(String mobileNumber);

    @Query(value = "SELECT A.* FROM TBL_ACCOUNT A INNER JOIN TBL_ACCOUNT_LEVEL AL on AL.ACCOUNT_LEVEL_ID=A.ACCOUNT_LEVEL_ID\n" +
        "INNER JOIN TBL_CUSTOMER C ON A.CUSTOMER_ID=C.CUSTOMER_ID \n" +
        "INNER JOIN TBL_CUSTOMER_ALL CL ON C.CUSTOMER_ALL_ID=CL.CUSTOMER_ALL_ID\n" +
        "WHERE AL.ACCOUNT_LEVEL_CODE=:s AND CL.MOBILE_NO=:mobileNumber AND C.NID_NO=:nidNumber", nativeQuery = true)
    TblAccount findByMobileNoAndCnicAndAccountLevelCode(String mobileNumber, String nidNumber, String s);

    @Query(value = "  SELECT A.* FROM TBL_ACCOUNT A\n" +
        "            INNER JOIN TBL_CUSTOMER C ON A.CUSTOMER_ID=C.CUSTOMER_ID \n" +
        "            INNER JOIN TBL_CUSTOMER_ALL CL ON C.CUSTOMER_ALL_ID=CL.CUSTOMER_ALL_ID\n" +
        "            WHERE CL.MOBILE_NO=:mobileNumber OR C.NID_NO=:nidNumber AND A.IS_ACTIVE='Y' AND A.IS_ACTIVE='Y'", nativeQuery = true)
    List<TblAccount> findCustomerTblAccountsByMobileOrCnic(String mobileNumber, String nidNumber);

    @Query(value = " SELECT A.* FROM TBL_ACCOUNT A\n" +
            "            INNER JOIN TBL_AGENT AG ON A.AGENT_ID=AG.AGENT_ID \n" +
            "            INNER JOIN TBL_CUSTOMER_ALL CL ON AG.CUSTOMER_ALL_ID=CL.CUSTOMER_ALL_ID\n" +
            "            WHERE CL.CUSTOMER_ALL_ID=:customerAllId", nativeQuery = true)
    TblAccount findByCustomerAllId(long customerAllId);

    @Query(value = "select A.account_no, A.account_title, l.account_level_code from TBL_ACCOUNT A " +
        " inner join tbl_account_level l on a.account_level_id = l.account_level_id " +
        " where A.mobile_no =:mobileNumber",nativeQuery = true)
    Object accountDetail(String mobileNumber);

    @Query(value = "SELECT A.*\n" +
        "                        FROM TBL_ACCOUNT A\n" +
        "                        INNER JOIN LKP_ACCOUNT_TYPE AT ON A.ACCOUNT_TYPE_ID = AT.ACCOUNT_TYPE_ID\n" +
        "                        WHERE A.ACCOUNT_NO=:accountNo\n" +
        "                          AND AT.ACCOUNT_TYPE_CODE=:accountTypeCode AND A.IS_ACTIVE ='Y'\n" +
        "                        AND A.ACCOUNT_STATUS_ID <>5", nativeQuery = true)
    TblAccount findByAccountNoAndAccountTypeCode(String accountNo, String accountTypeCode);
}
