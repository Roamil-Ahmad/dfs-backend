package com.dfs.backoffice.repo;

import com.dfs.backoffice.dto.SearchRole;
import com.dfs.backoffice.model.TblRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblRoleRepo extends JpaRepository<TblRole,Long> {

    @Query(value = "SELECT *\n" +
            "        FROM TBL_ROLE\n" +
            "    WHERE ROLE_DESCR = NVL(:#{#searchRole.roleDescr}, ROLE_DESCR)\n" +
            "        AND STATUS_ID = NVL(:#{#searchRole.statusId}, STATUS_ID) \n" +
            "        AND CREATEUSER = NVL(:#{#searchRole.createUser}, CREATEUSER)\n" +
            "        AND CAST(CREATEDATE AS DATE) BETWEEN NVL(TO_DATE(:dateFromInput,'YYYY-MM-DD HH24:MI:SS'), CAST(CREATEDATE AS DATE))\n" +
            "                                         AND NVL(TO_DATE(:dateToInput,'YYYY-MM-DD HH24:MI:SS'), CAST(CREATEDATE AS DATE))", nativeQuery = true)
    List<TblRole> getAllRoles(SearchRole searchRole, String dateFromInput, String dateToInput);

    List<TblRole> findByIsActive(String yes);
}
