package com.dfs.agentapp.repo;

import com.dfs.agentapp.model.LkpBusinessType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpBusinessTypeRepo extends JpaRepository<LkpBusinessType,Long> {
    /**
     * BUSINESS_TYPE_ID is numeric, so the id has to be bound as a number: the previous signature
     * took a String, which Spring Data tried to bind to a long property and rejected at runtime.
     */
    LkpBusinessType findByBusinessTypeIdAndIsActive(Long businessTypeId, String isActive);

    List<LkpBusinessType> findAllByIsActive(String isActive);
}
