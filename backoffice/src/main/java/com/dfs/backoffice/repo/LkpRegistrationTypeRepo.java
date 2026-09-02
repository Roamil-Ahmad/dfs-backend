package com.dfs.backoffice.repo;

import com.dfs.backoffice.model.LkpRegistrationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpRegistrationTypeRepo extends JpaRepository<LkpRegistrationType,Long> {
    LkpRegistrationType findByRegistrationTypeCode(String code);
}
