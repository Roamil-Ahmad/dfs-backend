package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpLicenseType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpLicenseTypeRepo extends JpaRepository<LkpLicenseType,Long> {
    LkpLicenseType findByLicenseTypeCodeAndIsActive(String licenseTypeCode, String yes);
}
