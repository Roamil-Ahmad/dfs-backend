package com.dfs.app.repo;

import com.dfs.app.model.LkpDebitCardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpDebitCardTypeRepo extends JpaRepository<LkpDebitCardType,Long> {
    LkpDebitCardType findByDebitCardTypeCode(String cardType);
}
