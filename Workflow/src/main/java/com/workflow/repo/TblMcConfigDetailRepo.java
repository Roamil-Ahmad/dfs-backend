package com.workflow.repo;

import com.workflow.modal.TblMcConfigDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblMcConfigDetailRepo extends JpaRepository<TblMcConfigDetail,Long> {
    List<TblMcConfigDetail> findByTblMcConfigMcConfigId(long mcConfigId);
}
