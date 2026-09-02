package com.wallet.transaction.repo;

import com.wallet.transaction.model.LkpBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LkpBankRepo extends JpaRepository<LkpBank,Long> {
    List<LkpBank> findAllByIsActive(String isActive);
    /** The bank the customer selected, matched on its 1LINK IMD and only while it is active. */
    @Query(value = "SELECT * FROM LKP_BANK B WHERE TRIM(B.BANK_IMD) = TRIM(:bankImd) AND B.IS_ACTIVE = :isActive",
            nativeQuery = true)
    LkpBank findByBankImdAndIsActive(@Param("bankImd") String bankImd, @Param("isActive") String isActive);
}
