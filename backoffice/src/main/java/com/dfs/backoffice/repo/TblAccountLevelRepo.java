package com.dfs.backoffice.repo;

import com.dfs.backoffice.dto.SearchAccountLevel;
import com.dfs.backoffice.model.TblAccountLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblAccountLevelRepo extends JpaRepository<TblAccountLevel,Long> {
    @Query(value = "SELECT *\n" +
            "        FROM TBL_ACCOUNT_LEVEL\n" +
            "    WHERE ACCOUNT_LEVEL_DESCR = NVL(:#{#searchAccountLevel.accountLevelDescr}, ACCOUNT_LEVEL_DESCR)\n" +
            "        AND GL_ACCOUNT_ID = NVL(:#{#searchAccountLevel.glAccountId}, GL_ACCOUNT_ID) \n" +
            "        AND CAST(CREATEDATE AS DATE) BETWEEN NVL(TO_DATE(:dateFromInput,'YYYY-MM-DD HH24:MI:SS'), CAST(CREATEDATE AS DATE))\n" +
            "                                         AND NVL(TO_DATE(:dateToInput,'YYYY-MM-DD HH24:MI:SS'), CAST(CREATEDATE AS DATE))", nativeQuery = true)
    List<TblAccountLevel> getAllAccountLevel(SearchAccountLevel searchAccountLevel, String dateFromInput, String dateToInput);

    TblAccountLevel findByAccountLevelCode(String accountLevelThree);
}
