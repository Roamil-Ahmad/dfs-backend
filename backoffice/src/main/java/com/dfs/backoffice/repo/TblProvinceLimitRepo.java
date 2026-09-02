package com.dfs.backoffice.repo;

import com.dfs.backoffice.dto.ProvinceLimitProjection;
import com.dfs.backoffice.model.TblProvinceLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TblProvinceLimitRepo extends JpaRepository<TblProvinceLimit, Long> {

  Optional<TblProvinceLimit> findByProvinceIdAndAccountLevelId(BigDecimal provinceId, BigDecimal accountLevelId);

  @Query("SELECT t.provinceLimitId as provinceLimitId, " +
      "t.provinceId as provinceId, " +
      "p.provinceCode as provinceCode, " +
      "p.provinceDescr as provinceDescr, " +
      "p.isActive as provinceActive, " +

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

      "FROM TblProvinceLimit t " +
      "JOIN LkpProvince p ON p.provinceId = t.provinceId " +
      "JOIN TblAccountLevel a ON a.accountLevelId = t.accountLevelId " +
      "WHERE (:provinceId IS NULL OR t.provinceId = :provinceId) " +
      "AND (:accountLevelId IS NULL OR t.accountLevelId = :accountLevelId) " +
      "AND (:riskProfile IS NULL OR t.riskProfile = :riskProfile) " +
      "AND (:dateFrom IS NULL OR t.createdate >= :dateFrom) " +
      "AND (:dateTo IS NULL OR t.createdate <= :dateTo)")
  List<ProvinceLimitProjection> searchProvinceLimits(
      BigDecimal provinceId,
      BigDecimal accountLevelId,
      String riskProfile,
      Date dateFrom,
      Date dateTo);
}