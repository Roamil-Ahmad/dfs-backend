package com.dfs.app.repo;


import com.dfs.app.model.LkpDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpDocumentTypeRepo extends JpaRepository<LkpDocumentType,Long> {
    LkpDocumentType findByDocumentTypeCode(String code);
}
