package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.model.TblAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TblAccountRepo extends JpaRepository<TblAccount, Long> {

    @Query(value = "SELECT * FROM TBL_ACCOUNT WHERE ACCOUNT_NO = :mobileNo OR MOBILE_NO = :mobileNo", nativeQuery = true)
    TblAccount checkAccountExistence(@Param("mobileNo") String mobileNo);

    @Query(value = "select r.*   from tbl_agent p  INNER JOIN tbl_account  R ON r.agent_id = p.agent_id  \n" +
            " where UPPER (p.NAME||p.cnic||p.mobile)  like '%'||:searchString||'%'", nativeQuery = true)
    List<TblAccount> searchAgentByUserNameCnicMobNo(@Param("searchString") String searchString);

    @Query(value = "select p.* \n" + "from TBL_ACCOUNT p\n" + "where p.agent_id = :agentId", nativeQuery = true)
    TblAccount searchAgentById(@Param("agentId") long agentId);

    @Query(value = "select p.* " + "from TBL_ACCOUNT p where " + "p.account_no = :accountNo", nativeQuery = true)
    TblAccount getAccountAgainstAccountNo(@Param("accountNo") String accountNo);

    @Query(value = "SELECT T.*\n" +
            "  FROM TBL_AGENT A\n" +
            " INNER JOIN TBL_ACCOUNT T ON A.AGENT_ID = T.AGENT_ID\n" +
            " WHERE A.PARENT_AGENT_ID = :agentId", nativeQuery = true)
    List<TblAccount> getChildAgentsOfLoggedAgent(@Param("agentId") long agentId);


    @Query(value = "select r.*   from tbl_customer c  INNER JOIN tbl_account  R ON r.CUSTOMER_ID = c.CUSTOMER_ID  \n" +
            " where UPPER (c.cnic)  like '%'||:searchString||'%'", nativeQuery = true)
    TblAccount searchCustomerByUserNameCnicMobNo(@Param("searchString") String searchString);


    @Query(value = "select p.* " + "from TBL_ACCOUNT p where " + "p.account_no = :accountNo and p.AGENT_ACCOUNT = 'N'", nativeQuery = true)
    TblAccount getCustomerAccountAgainstAccountNo(@Param("accountNo") String accountNo);
}
