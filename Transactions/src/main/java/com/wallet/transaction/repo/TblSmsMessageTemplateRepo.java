/*
Author Name: romail.ahmed

Project Name: transaction

Package Name: com.wallet.transaction.repo

Interface Name: TblSmsMessageTemplateRepo

Date and Time:1/4/2025 4:22 PM

Version:1.0
*/

package com.wallet.transaction.repo;

import com.wallet.transaction.model.TblSmsMessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TblSmsMessageTemplateRepo extends JpaRepository<TblSmsMessageTemplate, Long> {

    @Query(value = "select * from tbl_sms_message_template where trans_docs_id = :transDocsId", nativeQuery = true)
    List<TblSmsMessageTemplate> getByTransDocsId(@Param("transDocsId") BigDecimal transDocsId);
}
