package com.wallet.transaction.repo;

import com.wallet.transaction.model.LkpRegistrationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpRegistrationTypeRepo extends JpaRepository<LkpRegistrationType,Long> {
}
