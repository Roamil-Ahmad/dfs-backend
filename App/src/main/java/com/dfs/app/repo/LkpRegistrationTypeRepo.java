package com.dfs.app.repo;

import com.dfs.app.model.LkpRegistrationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpRegistrationTypeRepo extends JpaRepository<LkpRegistrationType,Long> {
    LkpRegistrationType findByRegistrationTypeCode(String code);
}
