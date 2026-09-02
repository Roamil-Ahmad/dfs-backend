package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.LkpCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LkpCityRepo extends JpaRepository<LkpCity,Long> {

    @Query(value = "SELECT * FROM LKP_CITY WHERE IS_ACTIVE = 'Y' AND DISTRICT_ID = :districtId", nativeQuery = true)
    List<LkpCity> lovCityWithDistrict(Long districtId);
}
