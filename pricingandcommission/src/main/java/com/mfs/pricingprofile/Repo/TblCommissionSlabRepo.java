package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.model.TblCommissionSlab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblCommissionSlabRepo extends JpaRepository<TblCommissionSlab, Long> {
    List<TblCommissionSlab> findByTblCommissionProfileCommissionProfileId(long commissionProfileId);

    TblCommissionSlab findByTblCommissionProfileCommissionProfileIdAndCommissionSlabId(long commissionProfileId, long commissionId);

    List<TblCommissionSlab> findByTblCommissionProfileCommissionProfileIdAndIsActive(long commissionProfileId, String y);
}
