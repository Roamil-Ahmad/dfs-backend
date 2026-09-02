package com.dfs.backoffice.repo;

import com.dfs.backoffice.dto.DistrictWithProvince;
import com.dfs.backoffice.model.LkpDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LkpDistrictRepo extends JpaRepository<LkpDistrict,Long> {

    @Query(value = "SELECT * FROM LKP_DISTRICT WHERE IS_ACTIVE = 'Y' AND PROVINCE_ID = :provinceId", nativeQuery = true)
    List<LkpDistrict> lovDistrictWithProvince(Long provinceId);

    LkpDistrict findByDistrictIdAndIsActive(long dictrictId, String yes);

    Optional<LkpDistrict> findByDistrictCodeIgnoreCase(String districtCode);

    @Query("SELECT new com.dfs.backoffice.dto.DistrictWithProvince(" +
        "d.districtId, d.districtCode, d.districtDescr, d.isActive, d.provinceId, d.riskProfile, " +
        "d.createUser, d.createDate, d.lastUpdateUser, d.lastUpdateDate, d.updateIndex, " +
        "p.provinceDescr) " +
        "FROM LkpDistrict d " +
        "LEFT JOIN LkpProvince p ON d.provinceId = p.provinceId " +
        "WHERE (:districtCode IS NULL OR LOWER(d.districtCode) LIKE LOWER(CONCAT('%', :districtCode, '%'))) " +
        "AND (:districtDescr IS NULL OR LOWER(d.districtDescr) LIKE LOWER(CONCAT('%', :districtDescr, '%'))) " +
        "AND (:provinceId IS NULL OR d.provinceId = :provinceId) " +
        "AND (:dateFrom IS NULL OR d.createDate >= :dateFrom) " +
        "AND (:dateTo IS NULL OR d.createDate <= :dateTo)")
    List<DistrictWithProvince> searchDistricts(
        @Param("districtCode") String districtCode,
        @Param("districtDescr") String districtDescr,
        @Param("provinceId") Long provinceId,
        @Param("dateFrom") Date dateFrom,
        @Param("dateTo") Date dateTo
    );
}
