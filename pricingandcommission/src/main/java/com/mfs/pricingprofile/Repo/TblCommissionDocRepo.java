package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.model.TblCommissionDoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblCommissionDocRepo extends JpaRepository<TblCommissionDoc, Long> {
    List<TblCommissionDoc> findByTblCommissionProfileCommissionProfileId(long commissionProfileId);

    TblCommissionDoc findByTblCommissionProfileCommissionProfileIdAndTblTransDocTransDocsId(long commissionProfileId, long longValue);

    List<TblCommissionDoc> findByTblCommissionProfileCommissionProfileIdAndIsActive(long commissionProfileId, String y);
}
