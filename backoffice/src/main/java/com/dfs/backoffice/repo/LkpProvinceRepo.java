package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.LkpProvince;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LkpProvinceRepo extends JpaRepository<LkpProvince,Long> {
    List<LkpProvince> findByIsActive(String yes);

    LkpProvince findByProvinceIdAndIsActive(long provinceId, String yes);

    Optional<LkpProvince> findByProvinceCodeIgnoreCase(String provinceCode);

    @Query("SELECT p FROM LkpProvince p " +
        "WHERE (:provinceCode IS NULL OR LOWER(p.provinceCode) LIKE LOWER(CONCAT('%', :provinceCode, '%'))) " +
        "AND (:provinceDescr IS NULL OR LOWER(p.provinceDescr) LIKE LOWER(CONCAT('%', :provinceDescr, '%'))) " +
        "AND (:dateFrom IS NULL OR p.createdate >= :dateFrom) " +
        "AND (:dateTo IS NULL OR p.createdate <= :dateTo)")
    List<LkpProvince> searchProvinces(
        @Param("provinceCode") String provinceCode,
        @Param("provinceDescr") String provinceDescr,
        @Param("dateFrom") Timestamp dateFrom,
        @Param("dateTo") Timestamp dateTo
    );
}
