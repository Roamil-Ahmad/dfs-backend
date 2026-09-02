package com.dfs.backoffice.repo;

import com.dfs.backoffice.dto.DistrictLimitProjection;
import com.dfs.backoffice.model.TblDistrictLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TblDistrictLimitRepo extends JpaRepository<TblDistrictLimit, Long> {

  Optional<TblDistrictLimit> findByDistrictIdAndAccountLevelId(BigDecimal districtId, BigDecimal accountLevelId);

  @Query("SELECT " +
      "t.districtLimitId as districtLimitId, " +

      // District info
      "t.districtId as districtId, " +
      "d.districtCode as districtCode, " +
      "d.districtDescr as districtDescr, " +
      "d.isActive as districtActive, " +

      "t.accountLevelId as accountLevelId, " +
      "a.accountLevelCode as accountLevelCode, " +
      "a.accountLevelDescr as accountLevelDescr, " +
      "a.isActive as accountLevelActive, " +
      "a.statusId as statusId, " +

      "t.riskProfile as riskProfile, " +
      "t.createdate as createdate, " +
      "t.createuser as createuser, " +
      "t.lastupdatedate as lastupdatedate, " +
      "t.lastupdateuser as lastupdateuser, " +

      "t.dailyAmtLimitCr as dailyAmtLimitCr, " +
      "t.dailyAmtLimitDr as dailyAmtLimitDr, " +
      "t.dailyTransLimitCr as dailyTransLimitCr, " +
      "t.dailyTransLimitDr as dailyTransLimitDr, " +

      "t.monthlyAmtLimitCr as monthlyAmtLimitCr, " +
      "t.monthlyAmtLimitDr as monthlyAmtLimitDr, " +
      "t.monthlyTransLimitCr as monthlyTransLimitCr, " +
      "t.monthlyTransLimitDr as monthlyTransLimitDr, " +

      "t.yearlyAmtLimitCr as yearlyAmtLimitCr, " +
      "t.yearlyAmtLimitDr as yearlyAmtLimitDr, " +
      "t.yearlyTransLimitCr as yearlyTransLimitCr, " +
      "t.yearlyTransLimitDr as yearlyTransLimitDr " +

      "FROM TblDistrictLimit t " +
      "JOIN LkpDistrict d ON d.districtId = t.districtId " +
      "JOIN TblAccountLevel a ON a.accountLevelId = t.accountLevelId " +

      "WHERE (:districtId IS NULL OR t.districtId = :districtId) " +
      "AND (:accountLevelId IS NULL OR t.accountLevelId = :accountLevelId) " +
      "AND (:riskProfile IS NULL OR t.riskProfile = :riskProfile) " +
      "AND (:dateFrom IS NULL OR t.createdate >= :dateFrom) " +
      "AND (:dateTo IS NULL OR t.createdate <= :dateTo)")
  List<DistrictLimitProjection> searchDistrictLimits(
      @Param("districtId") BigDecimal districtId,
      @Param("accountLevelId") BigDecimal accountLevelId,
      @Param("riskProfile") String riskProfile,
      @Param("dateFrom") Date dateFrom,
      @Param("dateTo") Date dateTo
  );
}