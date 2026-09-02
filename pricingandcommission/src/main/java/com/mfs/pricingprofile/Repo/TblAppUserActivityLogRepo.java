package com.mfs.pricingprofile.repo;

import com.mfs.pricingprofile.model.TblAppUserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TblAppUserActivityLogRepo extends JpaRepository<TblAppUserActivityLog, Long> {
}
