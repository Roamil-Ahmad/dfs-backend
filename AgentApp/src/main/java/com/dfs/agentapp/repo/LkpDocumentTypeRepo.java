package com.dfs.agentapp.repo;


import com.dfs.agentapp.model.LkpDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpDocumentTypeRepo extends JpaRepository<LkpDocumentType,Long> {
    LkpDocumentType findByDocumentTypeCode(String code);
}
