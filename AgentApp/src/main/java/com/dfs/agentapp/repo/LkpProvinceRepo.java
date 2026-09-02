package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpProvince;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpProvinceRepo extends JpaRepository<LkpProvince,Long> {

    List<LkpProvince> findAllByIsActive(String isActive);

    LkpProvince findByProvinceCodeAndIsActive(String code, String isActive);

    List<LkpProvince> findByProvinceDescrAndIsActive(String province, String yes);
}
