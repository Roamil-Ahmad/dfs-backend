package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.TblSmsMessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblSmsMessageTemplateRepo extends JpaRepository<TblSmsMessageTemplate, Long> {

    @Query(value = "SELECT T.* FROM TBL_SMS_MESSAGE_TEMPlATE T INNER JOIN TBL_TRANS_DOCS D ON T.TRANS_DOCS_ID=D.TRANS_DOCS_ID WHERE T.IDENTIFIER=:identifier AND D.TRANS_DOCS_CODE=:transDocCode", nativeQuery = true)
    TblSmsMessageTemplate findByIdentifierAndDocTypeCode(String identifier, String transDocCode);
}
