package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpOccupation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpOccupationRepo extends JpaRepository<LkpOccupation,Long> {
    List<LkpOccupation> findAllByIsActive(String isActive);

    LkpOccupation findByOccupationCodeAndIsActive(String occupationCode, String yes);
}
