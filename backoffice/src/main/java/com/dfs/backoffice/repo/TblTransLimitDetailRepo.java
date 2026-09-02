package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.TblTransLimitDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public interface TblTransLimitDetailRepo extends JpaRepository<TblTransLimitDetail,Long> {
    List<TblTransLimitDetail> findByTblTransLimitTransLimitId(long transLimitId);

    TblTransLimitDetail findByTblTransLimitTransLimitIdAndTransDocsId(long transLimitId, BigDecimal bigDecimal);
}
