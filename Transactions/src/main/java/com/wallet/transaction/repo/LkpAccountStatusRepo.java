package com.wallet.transaction.repo;

import com.wallet.transaction.model.LkpAccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpAccountStatusRepo extends JpaRepository<LkpAccountStatus,Long> {
}
