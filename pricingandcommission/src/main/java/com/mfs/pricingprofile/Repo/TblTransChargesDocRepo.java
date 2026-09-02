package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.model.TblTransChargesDoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TblTransChargesDocRepo extends JpaRepository<TblTransChargesDoc,Long> {
    List<TblTransChargesDoc> findByTblTransChargeTransChargesIdAndIsActive(long transChargesId, String status);
}
