package com.dfs.app.repo;

import com.dfs.app.model.TblDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblDocumentRepo extends JpaRepository<TblDocument,Long> {
    @Query(value = "SELECT * FROM TBL_DOCUMENT td WHERE DOCUMENT_TYPE_ID =:documentTypeId AND APP_USER_ID =:appUserId AND ACCOUNT_LEVEL_ID =:accountLevelId AND IS_ACTIVE ='Y'",nativeQuery = true)
    TblDocument findByDocumentTypeIdAndAppUserIdAndAccountLevelId(Long documentTypeId,Long appUserId,Long accountLevelId);
    @Query(value = "SELECT td.* FROM TBL_DOCUMENT td \n" +
            "INNER JOIN LKP_DOCUMENT_TYPE ldt ON LDT.DOCUMENT_TYPE_ID =TD.DOCUMENT_TYPE_ID\n" +
            "INNER JOIN TBL_APP_USER tau ON TAu.APP_USER_ID =td.APP_USER_ID \n" +
            "INNER JOIN TBL_ACCOUNT_LEVEL tal ON TAL.ACCOUNT_LEVEL_ID =TD.ACCOUNT_LEVEL_ID \n" +
            "WHERE tau.APP_USER_ID =:appUserId AND ldt.DOCUMENT_TYPE_CODE =:docTypeCode AND tal.ACCOUNT_LEVEL_CODE =:accountLevelCode AND TD.IS_ACTIVE ='Y'",nativeQuery = true)
    TblDocument findByAppUserIdDocTypeCodeAccountLevelCode(long appUserId,String docTypeCode,String accountLevelCode);
}
