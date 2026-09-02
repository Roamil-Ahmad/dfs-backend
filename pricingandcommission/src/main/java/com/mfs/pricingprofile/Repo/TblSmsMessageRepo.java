package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.model.TblSmsMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TblSmsMessageRepo extends JpaRepository<TblSmsMessage, Long> {

	@Query(value = "SELECT * FROM tbl_sms_message where SEND_FLAG = 'N'", nativeQuery = true)
	List<TblSmsMessage> getSmsMessageList();

	@Query(value = "select * from tbl_sms_message where trans_head_id = :transHeadId", nativeQuery = true)
	List<TblSmsMessage> getSmsMessageAgainstTransHeadId(@Param("transHeadId") long transHeadId);
	
}
