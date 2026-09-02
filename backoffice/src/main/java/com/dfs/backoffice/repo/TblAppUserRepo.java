package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.TblAppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface TblAppUserRepo extends JpaRepository<TblAppUser, Long> {

    @Query(value = "SELECT * FROM TBL_APP_USER WHERE UPPER( USERNAME )= :username AND PASSWORD = :password", nativeQuery = true)
    TblAppUser userLogin(String username, String password);

    @Query(value = "SELECT * FROM TBL_APP_USER WHERE APP_USER_ID= :userId AND PASSWORD = :oldPassword", nativeQuery = true)
    TblAppUser getUserByIdPassword(long userId, String oldPassword);

    TblAppUser findByUsername(String userName);

    @Query(value = "SELECT * FROM TBL_APP_USER", nativeQuery = true)
    List<TblAppUser> getAppUsers();

    TblAppUser findByUserId(BigDecimal userId);

    TblAppUser findByAgentId(BigDecimal userId);

    @Query(value = "SELECT * FROM TBL_APP_USER WHERE USERNAME= :username", nativeQuery = true)
    TblAppUser findAgentUsername(String username);

    @Query(value = " SELECT * " +
            "  FROM VW_AUDIT_LOG " +
            "WHERE USER_ID = NVL(:mobileNo, USER_ID) " +
            "   AND UPPER(USER_NAME) LIKE '%'||UPPER(:userName)||'%' " +
            "   AND TRUNC(ACTIVITY_DATE) BETWEEN NVL(TO_DATE(:fromDate,'YYYY-MM-DD'), TRUNC(ACTIVITY_DATE)) " +
            "                                AND NVL(TO_DATE(:toDate,'YYYY-MM-DD'), TRUNC(ACTIVITY_DATE)) ", nativeQuery = true)
    List<Object> auditLogRequest(String mobileNo, String userName, String fromDate, String toDate);
}
