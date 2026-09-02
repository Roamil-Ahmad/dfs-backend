package com.wallet.transaction.repo;

import com.wallet.transaction.model.LkpAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpAccountTypeRepo extends JpaRepository<LkpAccountType,Long> {
}
