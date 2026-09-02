package com.dfs.app.repo;

import com.dfs.app.model.LkpDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LkpDistrictRepo extends JpaRepository<LkpDistrict,Long> {
    @Query(value = "SELECT * FROM LKP_DISTRICT WHERE PROVINCE_ID=:id AND IS_ACTIVE='Y'",nativeQuery = true)
    List<LkpDistrict> findAllByProvinceId(Long id);

    @Query(value = "SELECT * FROM LKP_DISTRICT WHERE DISTRICT_CODE=:districtCode AND IS_ACTIVE='Y'",nativeQuery = true)
    LkpDistrict findByDistrictCode(String districtCode);
}
