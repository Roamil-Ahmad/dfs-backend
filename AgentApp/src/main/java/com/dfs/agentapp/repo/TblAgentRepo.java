/*
Author Name: romail.ahmed

Project Name: agentapp

Package Name: com.dfs.agentapp.repo

Interface Name: TblAgentRepo

Date and Time:2/5/2025 5:24 PM

Version:1.0
*/

package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.TblAgent;
import com.dfs.agentapp.model.TblCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblAgentRepo extends JpaRepository<TblAgent, Long> {


    @Query(value = " SELECT * FROM TBL_AGENT ta  WHERE ta.MOBILE =:mobileNo ", nativeQuery = true)
    TblAgent findByMobile(String mobileNo);
    @Query(value = "SELECT * FROM TBL_AGENT ta WHERE ta.AGENT_ID =:longValue AND ta.IS_ACTIVE =:yes ",nativeQuery = true)
    TblAgent findByIdAndIsActive(long longValue, String yes);
    @Query(value = "SELECT * FROM TBL_AGENT NID_NO =:nidNo",nativeQuery = true)
    TblAgent findByNidNo(String nidNo);
    @Query(value = "SELECT C.* FROM TBL_AGENT C INNER JOIN TBL_CUSTOMER_ALL A " +
            "on A.CUSTOMER_ALL_ID=C.CUSTOMER_ALL_ID" +
            " WHERE C.NID_NO=:nidNumber OR A.MOBILE_NO=:mobileNumber AND C.IS_ACTIVE='Y'",nativeQuery = true)
    TblAgent findByMobileNumberOrNidNo(String mobileNumber, String nidNumber);

    List<TblAgent> findByEmail(String email);

    TblAgent findByAgentId(long agentId);
}
