package com.wallet.transaction.repo;

import com.wallet.transaction.model.LkpTransPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LkpTransPurposeRepo extends JpaRepository<LkpTransPurpose,Long> {
    List<LkpTransPurpose> findByIsActive(String yes);
}
