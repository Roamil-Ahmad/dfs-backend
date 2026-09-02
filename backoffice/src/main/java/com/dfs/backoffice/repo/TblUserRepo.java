package com.dfs.backoffice.repo;

import com.dfs.backoffice.dto.UserSearch;
import com.dfs.backoffice.model.TblUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface TblUserRepo extends JpaRepository<TblUser, Long> {
    @Query(value = "SELECT * FROM TBL_USER WHERE USER_ID= :userId", nativeQuery = true)
    TblUser getUserById(long userId);

    @Query(value = "SELECT * FROM TBL_USER WHERE NID_NO = :cnic", nativeQuery = true)
    TblUser getUserByCnic(BigDecimal cnic);

    @Query(value = "SELECT * \n" +
            "  FROM TBL_USER U\n" +
            " INNER JOIN TBL_USER_ROLE R ON U.USER_ID = R.USER_ID \n" +
            " WHERE U.EMPLOYEE_NAME = NVL(:#{#userSearch.employeeName}, U.EMPLOYEE_NAME)\n" +
            "   AND U.EMAIL = NVL(:#{#userSearch.email}, U.EMAIL)\n" +
            "   AND U.MOBILE_NO = NVL(:#{#userSearch.mobileNo}, U.MOBILE_NO)\n" +
            "   AND U.EMPLOYEE_NO = NVL(:#{#userSearch.employeeNo}, U.EMPLOYEE_NO)\n" +
            "   AND U.STATUS_ID = NVL(:#{#userSearch.statusId}, U.STATUS_ID) \n" +
            "   AND R.ROLE_ID = NVL(:#{#userSearch.roleId}, R.ROLE_ID)\n" +
            "   AND CAST(U.CREATEDATE AS DATE) BETWEEN NVL(TO_DATE(:dateFromInput,'YYYY-MM-DD HH24:MI:SS'), CAST(U.CREATEDATE AS DATE))\n" +
            "                                      AND NVL(TO_DATE(:dateToInput,'YYYY-MM-DD HH24:MI:SS'), CAST(U.CREATEDATE AS DATE))", nativeQuery = true)
    List<TblUser> getAllUsers(UserSearch userSearch, String dateFromInput, String dateToInput);

    List<TblUser> findByIsActive(String yes);

    TblUser findByEmail(String email);

    @Query(value = "SELECT * FROM TBL_USER WHERE EMAIL=:email AND USER_ID <> :userId", nativeQuery = true)
    TblUser getUsersByEmail(String email, long userId);

    @Query(value = "SELECT * FROM TBL_USER WHERE MOBILE_NO = :mobileNumber", nativeQuery = true)
    TblUser getUserByMobileNo(String mobileNumber);
}
