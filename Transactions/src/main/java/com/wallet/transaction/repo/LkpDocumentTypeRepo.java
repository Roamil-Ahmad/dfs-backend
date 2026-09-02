package com.wallet.transaction.repo;


import com.wallet.transaction.model.LkpDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpDocumentTypeRepo extends JpaRepository<LkpDocumentType,Long> {
}
