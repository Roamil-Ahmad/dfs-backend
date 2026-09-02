package com.dfs.backoffice.repo;

import com.dfs.backoffice.dto.SearchTransDocs;
import com.dfs.backoffice.model.TblTransDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface TblTransDocRepo extends JpaRepository<TblTransDoc,Long> {

    @Query(value = "SELECT *\n" +
            "        FROM TBL_TRANS_DOCS\n" +
            "    WHERE TRANS_DOCS_DESCR = NVL(:#{#searchTransDocs.transDocsDescr}, TRANS_DOCS_DESCR)\n" +
            "        AND IS_ACTIVE = NVL(:#{#searchTransDocs.isActive}, IS_ACTIVE) \n" +
            "        AND CAST(CREATEDATE AS DATE) BETWEEN NVL(TO_DATE(:dateFromInput,'YYYY-MM-DD HH24:MI:SS'), CAST(CREATEDATE AS DATE))\n" +
            "                                         AND NVL(TO_DATE(:dateToInput,'YYYY-MM-DD HH24:MI:SS'), CAST(CREATEDATE AS DATE))", nativeQuery = true)
    List<TblTransDoc> getAllTransDocs(SearchTransDocs searchTransDocs, String dateFromInput, String dateToInput);

    List<TblTransDoc> findByTransTypeIdAndIsActive(BigDecimal typeId, String isActive);

    @Query(value = "SELECT * FROM TBL_TRANS_DOCS WHERE END_POINT =:endPoint AND IS_ACTIVE ='Y'",nativeQuery = true)
    TblTransDoc getTblTransDocByEndPoint(String endPoint);
}
