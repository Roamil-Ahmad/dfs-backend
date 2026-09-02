package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpRegistrationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpRegistrationTypeRepo extends JpaRepository<LkpRegistrationType,Long> {
    LkpRegistrationType findByRegistrationTypeCode(String code);
}
