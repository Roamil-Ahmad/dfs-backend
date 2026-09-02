package com.dfs.app.repo;

import com.dfs.app.model.LkpProvince;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LkpProvinceRepo extends JpaRepository<LkpProvince,Long> {

    List<LkpProvince> findAllByIsActive(String isActive);

    LkpProvince findByProvinceCodeAndIsActive(String code, String isActive);

    @Query(value = "SELECT * FROM LKP_PROVINCE WHERE UPPER(province_descr)=UPPER(:descr) and IS_ACTIVE=:isActive",nativeQuery = true)
    List<LkpProvince> findByProvinceDescrAndIsActive(String descr, String isActive);

}
