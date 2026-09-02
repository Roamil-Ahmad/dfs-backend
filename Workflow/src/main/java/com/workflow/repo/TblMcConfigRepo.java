package com.workflow.repo;

import com.workflow.modal.TblMcConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TblMcConfigRepo extends JpaRepository<TblMcConfig, Long> {

    TblMcConfig findByMcConfigId(long mcConfigId);

    TblMcConfig findByTableName(String tableName);

    @Query(value = "SELECT * \n" +
            "FROM TBL_MC_CONFIG \n" +
            "WHERE UPPER(CONFIG_NAME) = UPPER(NVL(:useCaseName, CONFIG_NAME)) \n" +
            "AND NVL(STATUS_ID,0) = NVL(:statusId, NVL(STATUS_ID,0)) \n" +
            "AND CREATEUSER = NVL(:createdBy,CREATEUSER) \n" +
            "AND NVL(LASTUPDATEUSER,0) = NVL(:updatedBy, NVL(LASTUPDATEUSER,0)) \n" +
            "AND CREATEDATE BETWEEN NVL(TO_DATE(:dateFromInput,'YYYY-MM-DD HH24:MI:SS'),CREATEDATE) AND NVL(TO_DATE(:dateToInput,'YYYY-MM-DD HH24:MI:SS'),CREATEDATE)", nativeQuery = true)
    List<TblMcConfig> getallusecases(String useCaseName, String createdBy, String updatedBy, String statusId, String dateFromInput, String dateToInput);

    List<TblMcConfig> findByIsActive(String y);
}
