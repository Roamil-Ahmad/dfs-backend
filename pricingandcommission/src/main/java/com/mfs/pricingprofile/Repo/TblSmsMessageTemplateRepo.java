package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.model.TblSmsMessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TblSmsMessageTemplateRepo extends JpaRepository<TblSmsMessageTemplate, Long> {

	@Query(value = "SELECT * FROM tbl_sms_message_template where trans_docs_id = :transDocId", nativeQuery = true)
	List<TblSmsMessageTemplate> getMessageTemplate(@Param("transDocId") long transId);

}
