package com.wallet.transaction.repo;

import com.wallet.transaction.model.TblDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TblDocumentRepo extends JpaRepository<TblDocument,Long> {
}
