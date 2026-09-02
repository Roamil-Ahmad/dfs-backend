package com.wallet.transaction.repo;


import com.wallet.transaction.model.TblAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TblAccountRepo extends JpaRepository<TblAccount, Long> {

    @Query(value = "SELECT a.* \n" +
            "FROM tbl_account a\n" +
            "INNER JOIN tbl_customer c \n" +
            "    ON c.customer_id = a.customer_id \n" +
            "   AND c.NID_NO = :cnic\n" +
            "INNER JOIN LKP_ACCOUNT_TYPE t \n" +
            "    ON t.ACCOUNT_TYPE_ID = a.ACCOUNT_TYPE_ID\n" +
            "INNER JOIN tbl_customer_all ca \n" +
            "    ON c.customer_all_id = ca.customer_all_id \n" +
            "   AND ca.imei_no = :imei\n" +
            "WHERE t.account_type_code = :accountTypeCode", nativeQuery = true)
    TblAccount getAccountByCnicImeiAndAccountType(@Param("cnic") String cnic, @Param("imei") String imei,
                                                  @Param("accountTypeCode") String accountTypeCode);


    @Query(value = "select iris_stan_seq.nextval from dual", nativeQuery = true)
    String getStan();

    @Query(value = "select a.*from tbl_account a\n" +
            "INNER JOIN LKP_ACCOUNT_TYPE t ON t.ACCOUNT_TYPE_ID =a.ACCOUNT_TYPE_ID where t.account_type_code = :accountTypeCode and (account_no = :accountNo OR IBAN = :accountNo)", nativeQuery = true)
    TblAccount getAccountByAccountNumberAndAccountType(@Param("accountNo") String accountNo,
                                                       @Param("accountTypeCode") String accountTypeCode);

    TblAccount findByAccountNo(String mobileNo);

    @Query(value = "SELECT A.* FROM TBL_ACCOUNT A " +
            "INNER JOIN LKP_ACCOUNT_TYPE T ON T.ACCOUNT_TYPE_ID = A.ACCOUNT_TYPE_ID " +
            "INNER JOIN TBL_AGENT AG ON A.AGENT_ID = AG.AGENT_ID " +
            "INNER JOIN TBL_APP_USER AU ON AU.AGENT_ID = AG.AGENT_ID " +
            "INNER JOIN TBL_DEVICE_INFO D ON D.APP_USER_ID = AU.APP_USER_ID " +
            "WHERE AG.NID_NO = :cnic " +
            "AND D.IMEI_NO = :imei " +
            "AND T.ACCOUNT_TYPE_CODE = :accountTypeCode " +
            // The app user join multiplies rows for a corporate agent, one per partner.
            "ORDER BY A.ACCOUNT_ID FETCH FIRST 1 ROW ONLY",
            nativeQuery = true)
    TblAccount getAgentAccountByCnicImeiAndAccountType(@Param("cnic") String cnic,
                                                       @Param("imei") String imei,
                                                       @Param("accountTypeCode") String accountTypeCode);


    @Query(value = "SELECT tc.NID_NO  FROM TBL_ACCOUNT ta INNER JOIN TBL_CUSTOMER tc ON ta.CUSTOMER_ID = tc.CUSTOMER_ID WHERE ta.ACCOUNT_NO =:accountNumber", nativeQuery = true)
    String findNidNoByAccountNo(String accountNumber);

    @Query(value = "SELECT AA.* FROM TBL_APP_USER A \n" +
            "            INNER JOIN TBL_AGENT AG ON AG.AGENT_ID=A.AGENT_ID \n" +
            "            INNER JOIN TBL_ACCOUNT AA ON AA.AGENT_ID = AG.AGENT_ID\n" +
            "            WHERE A.APP_USER_ID= :appUserId", nativeQuery = true)
    TblAccount findAgentByAppUserId(BigDecimal appUserId);

    @Query(value = "SELECT A.*\n" +
            "  FROM TBL_ACCOUNT A\n" +
            " INNER JOIN TBL_APP_USER AU ON (A.CUSTOMER_ID = AU.CUSTOMER_ID OR A.AGENT_ID = AU.AGENT_ID)\n" +
            " WHERE APP_USER_ID = :appUserId", nativeQuery = true)
    TblAccount getAccountByAppUserId(BigDecimal appUserId);

}
