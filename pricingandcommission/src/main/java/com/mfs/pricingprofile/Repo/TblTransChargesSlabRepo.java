package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.model.TblTransChargesSlab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblTransChargesSlabRepo extends JpaRepository<TblTransChargesSlab,Long> {
    List<TblTransChargesSlab> findByTblTransChargeTransChargesIdAndIsActive(long transChargesId, String status);

    List<TblTransChargesSlab> findByTblTransChargeTransChargesId(long transChargesId);
}
